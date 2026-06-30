package com.smartexam.module.grading.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartexam.common.exception.BizException;
import com.smartexam.common.result.ResultCode;
import com.smartexam.module.ai.service.AiGradingService;
import com.smartexam.module.exam.entity.ExamAnswer;
import com.smartexam.module.exam.entity.ExamRecord;
import com.smartexam.module.exam.mapper.ExamAnswerMapper;
import com.smartexam.module.exam.mapper.ExamRecordMapper;
import com.smartexam.module.exam.entity.ExamPaper;
import com.smartexam.module.exam.entity.ExamPaperQuestion;
import com.smartexam.module.exam.mapper.ExamPaperMapper;
import com.smartexam.module.exam.mapper.ExamPaperQuestionMapper;
import com.smartexam.module.grading.dto.BatchGradeDTO;
import com.smartexam.module.grading.dto.GradeAnswerDTO;
import com.smartexam.module.grading.vo.GradingPaperDetailVO;
import com.smartexam.module.grading.vo.GradingPaperVO;
import com.smartexam.module.grading.vo.GradingQuestionItem;
import com.smartexam.module.question.entity.Question;
import com.smartexam.module.question.entity.QuestionOption;
import com.smartexam.module.question.mapper.QuestionMapper;
import com.smartexam.module.question.mapper.QuestionOptionMapper;
import com.smartexam.module.user.entity.SysUser;
import com.smartexam.module.user.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 阅卷服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GradingService {

    private final ExamRecordMapper recordMapper;
    private final ExamAnswerMapper answerMapper;
    private final QuestionMapper questionMapper;
    private final QuestionOptionMapper questionOptionMapper;
    private final ExamPaperMapper paperMapper;
    private final ExamPaperQuestionMapper paperQuestionMapper;
    private final SysUserMapper userMapper;
    private final AiGradingService aiGradingService;

    /**
     * 获取待阅卷试卷列表
     */
    public Page<GradingPaperVO> getGradingPapers(int pageNum, int pageSize) {
        // 查询已提交的考试记录
        Page<ExamRecord> page = new Page<>(pageNum, pageSize);
        Page<ExamRecord> recordPage = recordMapper.selectPage(page,
                new LambdaQueryWrapper<ExamRecord>()
                        .in(ExamRecord::getStatus, "SUBMITTED", "GRADING")
                        .orderByDesc(ExamRecord::getSubmitTime)
        );

        // 按试卷分组统计
        Map<Long, List<ExamRecord>> grouped = recordPage.getRecords().stream()
                .collect(Collectors.groupingBy(ExamRecord::getPaperId));

        List<GradingPaperVO> voList = new ArrayList<>();
        for (Map.Entry<Long, List<ExamRecord>> entry : grouped.entrySet()) {
            Long paperId = entry.getKey();
            List<ExamRecord> records = entry.getValue();

            GradingPaperVO vo = new GradingPaperVO();
            vo.setPaperId(paperId);
            // 查询真实试卷标题
            ExamPaper paper = paperMapper.selectById(paperId);
            vo.setPaperTitle(paper != null ? paper.getTitle() : "试卷" + paperId);
            vo.setSubjectName(paper != null ? paper.getSubjectName() : "");
            vo.setSubmitCount(records.size());

            // 统计待阅卷和已阅卷
            long pendingCount = records.stream()
                    .filter(r -> "SUBMITTED".equals(r.getStatus()) || "GRADING".equals(r.getStatus()))
                    .count();
            vo.setPendingCount((int) pendingCount);
            vo.setGradedCount(records.size() - (int) pendingCount);

            voList.add(vo);
        }

        Page<GradingPaperVO> result = new Page<>(pageNum, pageSize);
        result.setRecords(voList);
        result.setTotal(voList.size());
        return result;
    }

    /**
     * 获取试卷答题列表
     */
    public Map<String, Object> getPaperAnswers(Long paperId, int pageNum, int pageSize) {
        // 查询该试卷的所有考试记录
        List<ExamRecord> records = recordMapper.selectList(
                new LambdaQueryWrapper<ExamRecord>()
                        .eq(ExamRecord::getPaperId, paperId)
                        .in(ExamRecord::getStatus, "SUBMITTED", "GRADING", "GRADED")
                        .orderByDesc(ExamRecord::getSubmitTime)
        );

        if (records.isEmpty()) {
            return Map.of("records", List.of(), "total", 0);
        }

        // 分页处理
        int total = records.size();
        int fromIndex = (pageNum - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, total);
        List<ExamRecord> pageRecords = records.subList(fromIndex, toIndex);

        // 查询每个记录的答题详情
        List<Map<String, Object>> detailList = new ArrayList<>();
        for (ExamRecord record : pageRecords) {
            Map<String, Object> detail = new HashMap<>();
            detail.put("recordId", record.getId());
            detail.put("userId", record.getUserId());
            detail.put("status", record.getStatus());
            detail.put("totalScore", record.getTotalScore());
            detail.put("submitTime", record.getSubmitTime());

            // 查询答题记录
            List<ExamAnswer> answers = answerMapper.selectList(
                    new LambdaQueryWrapper<ExamAnswer>()
                            .eq(ExamAnswer::getRecordId, record.getId())
            );
            detail.put("answers", answers);

            detailList.add(detail);
        }

        return Map.of("records", detailList, "total", total);
    }

    /**
     * 人工评分
     */
    @Transactional(rollbackFor = Exception.class)
    public void gradeAnswer(Long answerId, GradeAnswerDTO dto) {
        ExamAnswer answer = answerMapper.selectById(answerId);
        if (answer == null) {
            throw new BizException(ResultCode.ANSWER_NOT_FOUND);
        }

        // 校验分数
        Question question = questionMapper.selectById(answer.getQuestionId());
        if (question != null && dto.getFinalScore().compareTo(question.getScore()) > 0) {
            throw new BizException(ResultCode.SCORE_INVALID);
        }

        answer.setFinalScore(dto.getFinalScore());
        answer.setGradingStatus("TEACHER_DONE");
        answerMapper.updateById(answer);

        // 更新考试记录总分
        updateRecordTotalScore(answer.getRecordId());
    }

    /**
     * 触发AI批量阅卷
     */
    public void triggerAiGrade(Long paperId) {
        // 查询试卷题目分值映射
        List<ExamPaperQuestion> paperQuestions = paperQuestionMapper.selectList(
                new LambdaQueryWrapper<ExamPaperQuestion>()
                        .eq(ExamPaperQuestion::getPaperId, paperId)
        );
        Map<Long, BigDecimal> scoreMap = new HashMap<>();
        for (ExamPaperQuestion pq : paperQuestions) {
            scoreMap.put(pq.getQuestionId(), pq.getScore());
        }

        // 查询该试卷所有已提交的记录
        List<ExamRecord> records = recordMapper.selectList(
                new LambdaQueryWrapper<ExamRecord>()
                        .eq(ExamRecord::getPaperId, paperId)
                        .in(ExamRecord::getStatus, "SUBMITTED", "GRADING")
        );

        for (ExamRecord record : records) {
            // 查询待批改的答案
            List<ExamAnswer> answers = answerMapper.selectList(
                    new LambdaQueryWrapper<ExamAnswer>()
                            .eq(ExamAnswer::getRecordId, record.getId())
                            .eq(ExamAnswer::getGradingStatus, "PENDING")
            );

            for (ExamAnswer answer : answers) {
                Question question = questionMapper.selectById(answer.getQuestionId());
                if (question == null) continue;

                String type = question.getType();
                if ("SUBJECTIVE".equals(type) || "FILL".equals(type)) {
                    // 异步调用AI阅卷（主观题+填空题）
                    aiGradingService.gradeSubjectiveAsync(answer.getId());
                } else if ("SINGLE".equals(type) || "MULTI".equals(type) || "JUDGE".equals(type)) {
                    // 客观题自动判分
                    if (answer.getUserAnswer() != null && !answer.getUserAnswer().trim().isEmpty()) {
                        String correctAnswer = question.getAnswer();
                        boolean isCorrect = false;

                        if ("SINGLE".equals(type)) {
                            isCorrect = correctAnswer != null && correctAnswer.equalsIgnoreCase(answer.getUserAnswer().trim());
                        } else if ("JUDGE".equals(type)) {
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
            }

            // 更新记录状态为阅卷中
            record.setStatus("GRADING");
            recordMapper.updateById(record);
        }
    }

    /**
     * 排序答案（用于多选题比较）
     */
    private String sortAnswer(String answer) {
        if (answer == null || answer.isEmpty()) {
            return "";
        }
        return answer.chars()
                .sorted()
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.joining());
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
     * 获取试卷的学生提交列表
     */
    public List<Map<String, Object>> getPaperStudents(Long paperId) {
        List<ExamRecord> records = recordMapper.selectList(
                new LambdaQueryWrapper<ExamRecord>()
                        .eq(ExamRecord::getPaperId, paperId)
                        .in(ExamRecord::getStatus, "SUBMITTED", "GRADING", "GRADED")
                        .orderByDesc(ExamRecord::getSubmitTime)
        );

        List<Map<String, Object>> result = new ArrayList<>();
        for (ExamRecord record : records) {
            Map<String, Object> item = new HashMap<>();
            item.put("recordId", record.getId());
            item.put("userId", record.getUserId());
            item.put("status", record.getStatus());
            item.put("totalScore", record.getTotalScore());
            item.put("submitTime", record.getSubmitTime());

            // 查询学生姓名
            SysUser user = userMapper.selectById(record.getUserId());
            item.put("studentName", user != null ? (user.getRealName() != null ? user.getRealName() : user.getUsername()) : "未知");
            item.put("username", user != null ? user.getUsername() : "");

            result.add(item);
        }
        return result;
    }

    /**
     * 获取答卷详情（题目+选项+答案）
     */
    public GradingPaperDetailVO getRecordDetail(Long paperId, Long recordId) {
        // 查询考试记录
        ExamRecord record = recordMapper.selectById(recordId);
        if (record == null || !record.getPaperId().equals(paperId)) {
            throw new BizException(ResultCode.RECORD_NOT_FOUND);
        }

        // 查询试卷信息
        ExamPaper paper = paperMapper.selectById(paperId);

        // 查询学生信息
        SysUser user = userMapper.selectById(record.getUserId());

        // 构建VO
        GradingPaperDetailVO vo = new GradingPaperDetailVO();
        vo.setRecordId(recordId);
        vo.setUserId(record.getUserId());
        vo.setStudentName(user != null ? (user.getRealName() != null ? user.getRealName() : user.getUsername()) : "未知");
        vo.setPaperId(paperId);
        vo.setPaperTitle(paper != null ? paper.getTitle() : "试卷" + paperId);
        vo.setTotalScore(paper != null ? paper.getTotalScore() : BigDecimal.ZERO);
        vo.setStudentScore(record.getTotalScore());
        vo.setStatus(record.getStatus());
        vo.setSubmitTime(record.getSubmitTime());

        // 查询试卷题目关联（按sortOrder排序）
        List<ExamPaperQuestion> paperQuestions = paperQuestionMapper.selectList(
                new LambdaQueryWrapper<ExamPaperQuestion>()
                        .eq(ExamPaperQuestion::getPaperId, paperId)
                        .orderByAsc(ExamPaperQuestion::getSortOrder)
        );

        // 查询该记录的所有答案
        List<ExamAnswer> answers = answerMapper.selectList(
                new LambdaQueryWrapper<ExamAnswer>()
                        .eq(ExamAnswer::getRecordId, recordId)
        );
        Map<Long, ExamAnswer> answerMap = answers.stream()
                .collect(Collectors.toMap(ExamAnswer::getQuestionId, a -> a, (a, b) -> a));

        // 组装题目列表
        List<GradingQuestionItem> questionItems = new ArrayList<>();
        for (ExamPaperQuestion pq : paperQuestions) {
            Question question = questionMapper.selectById(pq.getQuestionId());
            if (question == null) continue;

            ExamAnswer answer = answerMap.get(pq.getQuestionId());

            GradingQuestionItem item = new GradingQuestionItem();
            item.setQuestionId(question.getId());
            item.setTitle(question.getTitle());
            item.setType(question.getType());
            item.setFullScore(pq.getScore());
            item.setReferenceAnswer(question.getAnswer());
            item.setExplanation(question.getExplanation());

            // 查询选项（选择题）
            if ("SINGLE".equals(question.getType()) || "MULTI".equals(question.getType())) {
                List<QuestionOption> options = questionOptionMapper.selectList(
                        new LambdaQueryWrapper<QuestionOption>()
                                .eq(QuestionOption::getQuestionId, question.getId())
                                .orderByAsc(QuestionOption::getSortOrder)
                );
                item.setOptions(options);
            }

            // 填充答案信息
            if (answer != null) {
                item.setAnswerId(answer.getId());
                item.setUserAnswer(answer.getUserAnswer());
                item.setAutoScore(answer.getScore());
                item.setAiScore(answer.getAiScore());
                item.setAiComment(answer.getAiComment());
                item.setFinalScore(answer.getFinalScore());
                item.setGradingStatus(answer.getGradingStatus());
                item.setIsCorrect(answer.getIsCorrect());
            }

            questionItems.add(item);
        }

        vo.setQuestions(questionItems);
        return vo;
    }

    /**
     * 批量评分
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchGrade(Long recordId, BatchGradeDTO dto) {
        for (BatchGradeDTO.GradeItem gradeItem : dto.getItems()) {
            ExamAnswer answer = answerMapper.selectById(gradeItem.getAnswerId());
            if (answer == null || !answer.getRecordId().equals(recordId)) {
                continue;
            }

            // 校验分数不超过满分
            Question question = questionMapper.selectById(answer.getQuestionId());
            if (question != null && gradeItem.getFinalScore().compareTo(question.getScore()) > 0) {
                throw new BizException(ResultCode.SCORE_INVALID);
            }

            answer.setFinalScore(gradeItem.getFinalScore());
            answer.setGradingStatus("TEACHER_DONE");
            answerMapper.updateById(answer);
        }

        // 更新考试记录总分
        updateRecordTotalScore(recordId);
    }

    /**
     * 更新考试记录总分
     */
    private void updateRecordTotalScore(Long recordId) {
        List<ExamAnswer> answers = answerMapper.selectList(
                new LambdaQueryWrapper<ExamAnswer>()
                        .eq(ExamAnswer::getRecordId, recordId)
        );

        BigDecimal totalScore = answers.stream()
                .map(a -> a.getFinalScore() != null ? a.getFinalScore() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        ExamRecord record = recordMapper.selectById(recordId);
        if (record != null) {
            record.setTotalScore(totalScore.setScale(2, RoundingMode.HALF_UP));
            record.setStatus("GRADED");
            recordMapper.updateById(record);
        }
    }
}
