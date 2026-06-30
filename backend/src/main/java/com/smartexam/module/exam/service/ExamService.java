package com.smartexam.module.exam.service;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartexam.common.exception.BizException;
import com.smartexam.common.result.ResultCode;
import com.smartexam.module.exam.algorithm.QuestionSelector;
import com.smartexam.module.exam.dto.ExamStartDTO;
import com.smartexam.module.exam.dto.ExamSubmitDTO;
import com.smartexam.module.exam.entity.*;
import com.smartexam.module.exam.mapper.*;
import com.smartexam.module.grading.service.GradingService;
import com.smartexam.module.grading.vo.GradingPaperDetailVO;
import com.smartexam.module.question.entity.Question;
import com.smartexam.module.question.entity.QuestionOption;
import com.smartexam.module.question.mapper.QuestionMapper;
import com.smartexam.module.question.mapper.QuestionOptionMapper;
import com.smartexam.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 考试服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamPaperMapper paperMapper;
    private final ExamRecordMapper recordMapper;
    private final ExamAnswerMapper answerMapper;
    private final ExamRuleMapper ruleMapper;
    private final ExamPaperQuestionMapper paperQuestionMapper;
    private final QuestionMapper questionMapper;
    private final QuestionOptionMapper questionOptionMapper;
    private final QuestionSelector questionSelector;
    private final ExamPaperService paperService;
    private final GradingService gradingService;
    private final RedisUtil redisUtil;
    private final ObjectMapper objectMapper;

    @Value("${draft.prefix}")
    private String draftPrefix;

    @Value("${draft.ttl}")
    private long draftTtl;

    /**
     * 开始考试
     */
    @Transactional(rollbackFor = Exception.class)
    public ExamStartDTO startExam(Long paperId) {
        Long userId = StpUtil.getLoginIdAsLong();

        // 1. 校验试卷状态
        ExamPaper paper = paperMapper.selectById(paperId);
        if (paper == null) {
            throw new BizException(ResultCode.PAPER_NOT_FOUND);
        }
        if (!"PUBLISHED".equals(paper.getStatus())) {
            throw new BizException(ResultCode.PAPER_NOT_PUBLISHED);
        }

        // 校验班级权限
        if (!paperService.canStudentAccessPaper(paperId, userId)) {
            throw new BizException(ResultCode.FAILURE.getCode(), "您所在的班级无权参加此考试");
        }

        LocalDateTime now = LocalDateTime.now();
        if (paper.getStartTime() != null && now.isBefore(paper.getStartTime())) {
            throw new BizException(ResultCode.PAPER_NOT_STARTED);
        }
        if (paper.getEndTime() != null && now.isAfter(paper.getEndTime())) {
            throw new BizException(ResultCode.PAPER_ENDED);
        }

        // 2. 检查考试次数
        Long existCount = recordMapper.selectCount(
                new LambdaQueryWrapper<ExamRecord>()
                        .eq(ExamRecord::getPaperId, paperId)
                        .eq(ExamRecord::getUserId, userId)
        );
        if (existCount >= paper.getMaxAttempts()) {
            throw new BizException(ResultCode.ATTEMPT_LIMIT_EXCEEDED);
        }

        // 3. 生成随机种子
        long seed = System.nanoTime() + userId;

        // 4. 查询组卷规则
        ExamRule rule = ruleMapper.selectOne(
                new LambdaQueryWrapper<ExamRule>()
                        .eq(ExamRule::getPaperId, paperId)
        );

        List<Question> questions;
        Map<Long, List<QuestionOption>> optionMap;
        Map<Long, Map<String, String>> originCorrectMap;
        Map<Long, Double> scoreMap;

        if (rule != null) {
            // 规则抽题模式
            QuestionSelector.SelectResult result = questionSelector.selectByRules(
                    rule.getRuleConfig(), paper.getSubjectId(), seed
            );
            questions = result.getQuestions();
            optionMap = result.getOptionMap();
            originCorrectMap = result.getOriginCorrectMap();
            scoreMap = result.getScoreMap();
        } else {
            // 手动组卷模式
            List<ExamPaperQuestion> paperQuestions = paperQuestionMapper.selectList(
                    new LambdaQueryWrapper<ExamPaperQuestion>()
                            .eq(ExamPaperQuestion::getPaperId, paperId)
                            .orderByAsc(ExamPaperQuestion::getSortOrder)
            );
            questions = new ArrayList<>();
            optionMap = new HashMap<>();
            originCorrectMap = new HashMap<>();
            scoreMap = new HashMap<>();

            for (ExamPaperQuestion pq : paperQuestions) {
                Question q = questionMapper.selectById(pq.getQuestionId());
                if (q != null) {
                    questions.add(q);
                    scoreMap.put(q.getId(), pq.getScore().doubleValue());

                    // 查询选项
                    List<QuestionOption> options = questionOptionMapper.selectList(
                            new LambdaQueryWrapper<QuestionOption>()
                                    .eq(QuestionOption::getQuestionId, q.getId())
                                    .orderByAsc(QuestionOption::getSortOrder)
                    );
                    optionMap.put(q.getId(), options);
                }
            }

            // 乱序处理
            if (paper.getShuffleQuestion() == 1) {
                Random random = new Random(seed);
                Collections.shuffle(questions, random);
            }
        }

        // 5. 构建题目顺序JSON
        List<Long> questionOrder = questions.stream()
                .map(Question::getId)
                .collect(Collectors.toList());

        // 6. 构建选项顺序JSON
        Map<Long, List<String>> optionOrderMap = new HashMap<>();
        for (Map.Entry<Long, List<QuestionOption>> entry : optionMap.entrySet()) {
            List<String> labels = entry.getValue().stream()
                    .map(QuestionOption::getOptionLabel)
                    .collect(Collectors.toList());
            optionOrderMap.put(entry.getKey(), labels);
        }

        // 7. 创建考试记录
        ExamRecord record = new ExamRecord();
        record.setPaperId(paperId);
        record.setUserId(userId);
        record.setAttemptNo(existCount.intValue() + 1);
        record.setStatus("IN_PROGRESS");
        record.setStartTime(now);
        record.setEndTime(now.plusMinutes(paper.getDuration()));
        record.setShuffleSeed(seed);
        try {
            record.setQuestionOrder(objectMapper.writeValueAsString(questionOrder));
            record.setOptionOrder(objectMapper.writeValueAsString(optionOrderMap));
        } catch (Exception e) {
            throw new BizException(ResultCode.FAILURE.getCode(), "序列化题目顺序失败");
        }
        recordMapper.insert(record);

        // 8. 创建答题记录
        for (Question q : questions) {
            ExamAnswer answer = new ExamAnswer();
            answer.setRecordId(record.getId());
            answer.setQuestionId(q.getId());
            answer.setGradingStatus("PENDING");
            answerMapper.insert(answer);
        }

        // 9. 返回数据
        ExamStartDTO result = new ExamStartDTO();
        result.setRecordId(record.getId());
        result.setPaperTitle(paper.getTitle());
        result.setDuration(paper.getDuration());
        result.setTotalScore(paper.getTotalScore());
        result.setStartTime(record.getStartTime());
        result.setEndTime(record.getEndTime());
        result.setQuestionCount(questions.size());
        return result;
    }

    /**
     * 获取考试题目ID列表（用于答题卡）
     */
    public Map<String, Object> getQuestionIds(Long recordId) {
        Long userId = StpUtil.getLoginIdAsLong();

        ExamRecord record = recordMapper.selectById(recordId);
        if (record == null || !record.getUserId().equals(userId)) {
            throw new BizException(ResultCode.RECORD_NOT_FOUND);
        }

        List<Long> questionOrder;
        try {
            questionOrder = objectMapper.readValue(record.getQuestionOrder(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Long.class));
        } catch (Exception e) {
            throw new BizException("题目顺序数据异常");
        }

        // 查询所有已保存的答案
        Map<Long, String> answerMap = new HashMap<>();
        List<ExamAnswer> allAnswers = answerMapper.selectList(
                new LambdaQueryWrapper<ExamAnswer>()
                        .eq(ExamAnswer::getRecordId, recordId)
        );
        allAnswers.forEach(a -> {
            if (a.getUserAnswer() != null && !a.getUserAnswer().trim().isEmpty()) {
                answerMap.put(a.getQuestionId(), a.getUserAnswer());
            }
        });

        return Map.of(
                "questionIds", questionOrder,
                "total", questionOrder.size(),
                "answers", answerMap
        );
    }

    /**
     * 获取试卷题目（分页）
     */
    public Map<String, Object> getQuestions(Long recordId, int page, int size) {
        Long userId = StpUtil.getLoginIdAsLong();

        // 校验考试记录
        ExamRecord record = recordMapper.selectById(recordId);
        if (record == null || !record.getUserId().equals(userId)) {
            throw new BizException(ResultCode.RECORD_NOT_FOUND);
        }

        // 获取题目顺序
        List<Long> questionOrder;
        try {
            questionOrder = objectMapper.readValue(record.getQuestionOrder(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Long.class));
        } catch (Exception e) {
            throw new BizException("题目顺序数据异常");
        }

        // 计算分页
        int total = questionOrder.size();
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, total);

        if (fromIndex >= total) {
            return Map.of("questions", List.of(), "total", total, "page", page);
        }

        // 获取当前页题目ID
        List<Long> pageQuestionIds = questionOrder.subList(fromIndex, toIndex);

        // 查询题目信息（保留null占位，与questionOrder对齐）
        List<Question> questions = pageQuestionIds.stream()
                .map(id -> {
                    Question q = questionMapper.selectById(id);
                    if (q != null) {
                        // 查询选项
                        List<QuestionOption> options = questionOptionMapper.selectList(
                                new LambdaQueryWrapper<QuestionOption>()
                                        .eq(QuestionOption::getQuestionId, id)
                                        .orderByAsc(QuestionOption::getSortOrder)
                        );
                        q.setOptions(options);
                    }
                    return q;
                })
                .collect(Collectors.toList());

        // 查询已保存的答案
        Map<Long, String> answerMap = new HashMap<>();
        List<ExamAnswer> answers = answerMapper.selectList(
                new LambdaQueryWrapper<ExamAnswer>()
                        .eq(ExamAnswer::getRecordId, recordId)
                        .in(ExamAnswer::getQuestionId, pageQuestionIds)
        );
        answers.forEach(a -> answerMap.put(a.getQuestionId(), a.getUserAnswer()));

        return Map.of(
                "questions", questions,
                "answers", answerMap,
                "total", total,
                "page", page,
                "pageSize", size
        );
    }

    /**
     * 保存草稿到Redis
     */
    public void saveDraft(Long recordId, Map<Long, String> answers) {
        Long userId = StpUtil.getLoginIdAsLong();
        String key = draftPrefix + recordId + ":" + userId;
        redisUtil.set(key, answers, draftTtl, TimeUnit.SECONDS);
    }

    /**
     * 获取草稿
     */
    public Map<Long, String> getDraft(Long recordId) {
        Long userId = StpUtil.getLoginIdAsLong();
        String key = draftPrefix + recordId + ":" + userId;
        Object draft = redisUtil.get(key);
        if (draft instanceof Map) {
            return (Map<Long, String>) draft;
        }
        return new HashMap<>();
    }

    /**
     * 提交单题答案
     */
    public void submitAnswer(Long recordId, Long questionId, String answer) {
        Long userId = StpUtil.getLoginIdAsLong();

        // 校验考试记录
        ExamRecord record = recordMapper.selectById(recordId);
        if (record == null || !record.getUserId().equals(userId)) {
            throw new BizException(ResultCode.RECORD_NOT_FOUND);
        }
        if (!"IN_PROGRESS".equals(record.getStatus())) {
            throw new BizException(ResultCode.RECORD_NOT_IN_PROGRESS);
        }

        // 更新或插入答案
        ExamAnswer existAnswer = answerMapper.selectOne(
                new LambdaQueryWrapper<ExamAnswer>()
                        .eq(ExamAnswer::getRecordId, recordId)
                        .eq(ExamAnswer::getQuestionId, questionId)
        );

        if (existAnswer != null) {
            existAnswer.setUserAnswer(answer);
            answerMapper.updateById(existAnswer);
        } else {
            ExamAnswer newAnswer = new ExamAnswer();
            newAnswer.setRecordId(recordId);
            newAnswer.setQuestionId(questionId);
            newAnswer.setUserAnswer(answer);
            newAnswer.setGradingStatus("PENDING");
            answerMapper.insert(newAnswer);
        }
    }

    /**
     * 交卷
     */
    @Transactional(rollbackFor = Exception.class)
    public void submitExam(Long recordId) {
        Long userId = StpUtil.getLoginIdAsLong();

        // 校验考试记录
        ExamRecord record = recordMapper.selectById(recordId);
        if (record == null || !record.getUserId().equals(userId)) {
            throw new BizException(ResultCode.RECORD_NOT_FOUND);
        }
        if (!"IN_PROGRESS".equals(record.getStatus())) {
            throw new BizException(ResultCode.RECORD_ALREADY_SUBMITTED);
        }

        // 先从Redis同步草稿到数据库（此时状态还是IN_PROGRESS）
        Map<Long, String> draft = getDraft(recordId);
        if (draft != null && !draft.isEmpty()) {
            for (Map.Entry<Long, String> entry : draft.entrySet()) {
                submitAnswer(recordId, entry.getKey(), entry.getValue());
            }
        }

        // 客观题自动判分
        autoGradeObjectiveAnswers(record);

        // 更新记录状态
        record.setStatus("SUBMITTED");
        record.setSubmitTime(LocalDateTime.now());
        recordMapper.updateById(record);

        // 清除草稿
        String key = draftPrefix + recordId + ":" + userId;
        redisUtil.delete(key);
    }

    /**
     * 客观题自动判分
     */
    private void autoGradeObjectiveAnswers(ExamRecord record) {
        // 查询试卷题目分值映射
        List<ExamPaperQuestion> paperQuestions = paperQuestionMapper.selectList(
                new LambdaQueryWrapper<ExamPaperQuestion>()
                        .eq(ExamPaperQuestion::getPaperId, record.getPaperId())
        );
        Map<Long, BigDecimal> scoreMap = new HashMap<>();
        for (ExamPaperQuestion pq : paperQuestions) {
            scoreMap.put(pq.getQuestionId(), pq.getScore());
        }

        // 查询所有答案
        List<ExamAnswer> answers = answerMapper.selectList(
                new LambdaQueryWrapper<ExamAnswer>()
                        .eq(ExamAnswer::getRecordId, record.getId())
        );

        for (ExamAnswer answer : answers) {
            if (answer.getUserAnswer() == null || answer.getUserAnswer().trim().isEmpty()) {
                continue;
            }

            Question question = questionMapper.selectById(answer.getQuestionId());
            if (question == null) continue;

            String type = question.getType();
            if (!"SINGLE".equals(type) && !"MULTI".equals(type) && !"JUDGE".equals(type)) {
                continue;
            }

            String correctAnswer = question.getAnswer();
            boolean isCorrect = false;

            if ("SINGLE".equals(type)) {
                isCorrect = correctAnswer != null && correctAnswer.equalsIgnoreCase(answer.getUserAnswer().trim());
            } else if ("JUDGE".equals(type)) {
                // 学生答案可能是 T/F 或 true/false，统一标准化比较
                String userAns = normalizeJudgeAnswer(answer.getUserAnswer().trim());
                String correctAns = normalizeJudgeAnswer(correctAnswer);
                isCorrect = correctAns != null && correctAns.equals(userAns);
            } else if ("MULTI".equals(type)) {
                String sortedUser = sortAnswer(answer.getUserAnswer());
                String sortedCorrect = sortAnswer(correctAnswer);
                isCorrect = sortedCorrect != null && sortedCorrect.equals(sortedUser);
            }

            BigDecimal fullScore = scoreMap.getOrDefault(answer.getQuestionId(), BigDecimal.ZERO);
            answer.setIsCorrect(isCorrect ? 1 : 0);
            answer.setScore(isCorrect ? fullScore : BigDecimal.ZERO);
            answer.setFinalScore(isCorrect ? fullScore : BigDecimal.ZERO);
            answer.setGradingStatus("AUTO_GRADED");
            answerMapper.updateById(answer);
        }
    }

    /**
     * 排序答案（用于多选题比较）
     * 支持两种格式：ABD 或 A,B,D
     */
    private String sortAnswer(String answer) {
        if (answer == null || answer.isEmpty()) {
            return "";
        }
        // 去除逗号和空格，只保留字母
        String cleaned = answer.replace(",", "").replace(" ", "").trim();
        return cleaned.chars()
                .sorted()
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.joining());
    }

    /**
     * 重新判分指定考试记录的客观题（修复已存在的判分错误）
     */
    public void reGradeObjectiveAnswers(Long recordId) {
        ExamRecord record = recordMapper.selectById(recordId);
        if (record == null) {
            throw new BizException(ResultCode.RECORD_NOT_FOUND);
        }
        autoGradeObjectiveAnswers(record);

        // 重新计算总分
        List<ExamAnswer> answers = answerMapper.selectList(
                new LambdaQueryWrapper<ExamAnswer>()
                        .eq(ExamAnswer::getRecordId, recordId)
        );
        BigDecimal totalScore = answers.stream()
                .map(a -> a.getFinalScore() != null ? a.getFinalScore() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        record.setTotalScore(totalScore);
        // 更新状态为GRADED，以便试卷复查页面可以看到
        record.setStatus("GRADED");
        recordMapper.updateById(record);
    }

    /**
     * 标准化判断题答案（T/F → true/false）
     */
    private String normalizeJudgeAnswer(String answer) {
        if (answer == null) return null;
        String upper = answer.trim().toUpperCase();
        if ("T".equals(upper) || "TRUE".equals(upper)) return "true";
        if ("F".equals(upper) || "FALSE".equals(upper)) return "false";
        return answer.trim().toLowerCase();
    }

    /**
     * 获取学生已批改的考试记录列表（试卷复查）
     */
    public List<Map<String, Object>> getReviewList() {
        Long userId = StpUtil.getLoginIdAsLong();
        List<ExamRecord> records = recordMapper.selectList(
                new LambdaQueryWrapper<ExamRecord>()
                        .eq(ExamRecord::getUserId, userId)
                        .eq(ExamRecord::getStatus, "GRADED")
                        .orderByDesc(ExamRecord::getSubmitTime)
        );
        List<Map<String, Object>> result = new ArrayList<>();
        for (ExamRecord record : records) {
            // 检查试卷是否已被删除（逻辑删除的试卷selectById会返回null）
            ExamPaper paper = paperMapper.selectById(record.getPaperId());
            if (paper == null) {
                // 试卷已被删除，跳过该记录
                continue;
            }
            Map<String, Object> item = new HashMap<>();
            item.put("recordId", record.getId());
            item.put("paperId", record.getPaperId());
            item.put("paperTitle", paper.getTitle());
            item.put("subjectName", paper.getSubjectName());
            item.put("totalScore", paper.getTotalScore());
            item.put("studentScore", record.getTotalScore());
            item.put("submitTime", record.getSubmitTime());
            result.add(item);
        }
        return result;
    }

    /**
     * 获取单次考试的复查详情（复用阅卷中心的详情构建逻辑）
     */
    public GradingPaperDetailVO getReviewDetail(Long recordId) {
        Long userId = StpUtil.getLoginIdAsLong();
        ExamRecord record = recordMapper.selectById(recordId);
        if (record == null || !record.getUserId().equals(userId)) {
            throw new BizException(ResultCode.RECORD_NOT_FOUND);
        }
        if (!"GRADED".equals(record.getStatus())) {
            throw new BizException(ResultCode.FAILURE.getCode(), "试卷尚未批改完成");
        }
        return gradingService.getRecordDetail(record.getPaperId(), recordId);
    }
}
