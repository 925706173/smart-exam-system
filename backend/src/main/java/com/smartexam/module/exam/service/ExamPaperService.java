package com.smartexam.module.exam.service;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartexam.common.exception.BizException;
import com.smartexam.common.result.ResultCode;
import com.smartexam.module.class_.service.ClassService;
import com.smartexam.module.exam.entity.ExamPaper;
import com.smartexam.module.exam.entity.ExamPaperClass;
import com.smartexam.module.exam.entity.ExamPaperQuestion;
import com.smartexam.module.exam.entity.ExamRule;
import com.smartexam.module.exam.mapper.ExamPaperClassMapper;
import com.smartexam.module.exam.mapper.ExamPaperMapper;
import com.smartexam.module.exam.mapper.ExamPaperQuestionMapper;
import com.smartexam.module.exam.mapper.ExamRuleMapper;
import com.smartexam.module.question.entity.Question;
import com.smartexam.module.question.entity.QuestionOption;
import com.smartexam.module.question.mapper.QuestionMapper;
import com.smartexam.module.question.mapper.QuestionOptionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 试卷服务
 */
@Service
@RequiredArgsConstructor
public class ExamPaperService {

    private final ExamPaperMapper paperMapper;
    private final ExamRuleMapper ruleMapper;
    private final ExamPaperQuestionMapper paperQuestionMapper;
    private final ExamPaperClassMapper paperClassMapper;
    private final QuestionMapper questionMapper;
    private final QuestionOptionMapper questionOptionMapper;
    private final ClassService classService;

    /**
     * 分页查询试卷（学生只返回其班级关联的试卷）
     */
    public Page<ExamPaper> page(String keyword, String status, int pageNum, int pageSize, Long userId, String role) {
        Page<ExamPaper> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<ExamPaper> wrapper = new LambdaQueryWrapper<>();

        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(ExamPaper::getTitle, keyword);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(ExamPaper::getStatus, status);
        }

        // 学生角色：只返回其班级关联的试卷，未指定班级的试卷所有学生可见
        if ("STUDENT".equals(role) && userId != null) {
            List<Long> userClassIds = classService.getUserClassIds(userId);
            // 查询已指定班级的试卷ID
            List<ExamPaperClass> paperClasses = paperClassMapper.selectList(new LambdaQueryWrapper<>());
            List<Long> assignedPaperIds = paperClasses.stream().map(ExamPaperClass::getPaperId).distinct().collect(Collectors.toList());

            if (assignedPaperIds.isEmpty()) {
                // 没有试卷指定过班级，所有试卷对学生可见（仅按状态过滤）
                // 不添加额外条件
            } else {
                // 查询学生班级关联的试卷ID
                List<Long> userPaperIds = paperClasses.stream()
                        .filter(pc -> userClassIds.contains(pc.getClassId()))
                        .map(ExamPaperClass::getPaperId)
                        .distinct()
                        .collect(Collectors.toList());

                if (userPaperIds.isEmpty()) {
                    // 学生没有班级或班级没有关联试卷，只能看到未指定班级的试卷
                    wrapper.notIn(ExamPaper::getId, assignedPaperIds);
                } else {
                    // 未指定班级的试卷 + 学生班级关联的试卷
                    wrapper.and(w -> w
                            .notIn(ExamPaper::getId, assignedPaperIds)
                            .or()
                            .in(ExamPaper::getId, userPaperIds)
                    );
                }
            }
        }

        wrapper.orderByDesc(ExamPaper::getCreateTime);
        return paperMapper.selectPage(page, wrapper);
    }

    /**
     * 获取试卷详情
     */
    public ExamPaper getDetail(Long id) {
        ExamPaper paper = paperMapper.selectById(id);
        if (paper == null) {
            throw new BizException(ResultCode.PAPER_NOT_FOUND);
        }
        return paper;
    }

    /**
     * 创建试卷（手动组卷）
     */
    @Transactional(rollbackFor = Exception.class)
    public Long create(ExamPaper paper) {
        Long userId = StpUtil.getLoginIdAsLong();
        paper.setCreateBy(userId);
        paper.setStatus("DRAFT");
        paperMapper.insert(paper);
        return paper.getId();
    }

    /**
     * 规则抽题组卷
     */
    @Transactional(rollbackFor = Exception.class)
    public Long generate(ExamPaper paper, String ruleConfig) {
        Long userId = StpUtil.getLoginIdAsLong();
        paper.setCreateBy(userId);
        paper.setStatus("DRAFT");
        paperMapper.insert(paper);

        // 保存组卷规则
        ExamRule rule = new ExamRule();
        rule.setPaperId(paper.getId());
        rule.setRuleConfig(ruleConfig);
        ruleMapper.insert(rule);

        return paper.getId();
    }

    /**
     * 更新试卷
     */
    public void update(ExamPaper paper) {
        paperMapper.updateById(paper);
    }

    /**
     * 发布试卷
     */
    public void publish(Long id) {
        ExamPaper paper = paperMapper.selectById(id);
        if (paper == null) {
            throw new BizException(ResultCode.PAPER_NOT_FOUND);
        }
        paper.setStatus("PUBLISHED");
        paperMapper.updateById(paper);
    }

    /**
     * 删除试卷
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        paperMapper.deleteById(id);
        ruleMapper.delete(
                new LambdaQueryWrapper<ExamRule>()
                        .eq(ExamRule::getPaperId, id)
        );
        paperQuestionMapper.delete(
                new LambdaQueryWrapper<ExamPaperQuestion>()
                        .eq(ExamPaperQuestion::getPaperId, id)
        );
    }

    /**
     * 获取试卷题目列表
     */
    public List<Map<String, Object>> getPaperQuestions(Long paperId) {
        List<ExamPaperQuestion> pqList = paperQuestionMapper.selectList(
                new LambdaQueryWrapper<ExamPaperQuestion>()
                        .eq(ExamPaperQuestion::getPaperId, paperId)
                        .orderByAsc(ExamPaperQuestion::getSortOrder)
        );

        List<Map<String, Object>> result = new ArrayList<>();
        for (ExamPaperQuestion pq : pqList) {
            Question q = questionMapper.selectById(pq.getQuestionId());
            if (q == null) continue;

            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", pq.getId());
            item.put("questionId", q.getId());
            item.put("title", q.getTitle());
            item.put("type", q.getType());
            item.put("difficulty", q.getDifficulty());
            item.put("score", pq.getScore());
            item.put("sortOrder", pq.getSortOrder());
            item.put("answer", q.getAnswer());

            // 查询选项
            List<QuestionOption> options = questionOptionMapper.selectList(
                    new LambdaQueryWrapper<QuestionOption>()
                            .eq(QuestionOption::getQuestionId, q.getId())
                            .orderByAsc(QuestionOption::getSortOrder)
            );
            item.put("options", options);

            result.add(item);
        }
        return result;
    }

    /**
     * 向试卷添加题目
     */
    @Transactional(rollbackFor = Exception.class)
    public void addQuestion(Long paperId, Long questionId, BigDecimal score) {
        // 查询当前最大排序号
        Long count = paperQuestionMapper.selectCount(
                new LambdaQueryWrapper<ExamPaperQuestion>()
                        .eq(ExamPaperQuestion::getPaperId, paperId)
        );

        ExamPaperQuestion pq = new ExamPaperQuestion();
        pq.setPaperId(paperId);
        pq.setQuestionId(questionId);
        pq.setScore(score);
        pq.setSortOrder(count.intValue() + 1);
        paperQuestionMapper.insert(pq);

        // 更新试卷总分
        recalcTotalScore(paperId);
    }

    /**
     * 更新试卷题目（分值、排序）
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePaperQuestion(Long paperId, Long pqId, BigDecimal score, Integer sortOrder) {
        ExamPaperQuestion pq = paperQuestionMapper.selectById(pqId);
        if (pq == null || !pq.getPaperId().equals(paperId)) {
            throw new BizException(ResultCode.FAILURE.getCode(), "题目不存在");
        }
        if (score != null) pq.setScore(score);
        if (sortOrder != null) pq.setSortOrder(sortOrder);
        paperQuestionMapper.updateById(pq);

        recalcTotalScore(paperId);
    }

    /**
     * 从试卷移除题目
     */
    @Transactional(rollbackFor = Exception.class)
    public void removeQuestion(Long paperId, Long pqId) {
        ExamPaperQuestion pq = paperQuestionMapper.selectById(pqId);
        if (pq == null || !pq.getPaperId().equals(paperId)) {
            throw new BizException(ResultCode.FAILURE.getCode(), "题目不存在");
        }
        paperQuestionMapper.deleteById(pqId);

        // 重新排列排序号
        List<ExamPaperQuestion> remaining = paperQuestionMapper.selectList(
                new LambdaQueryWrapper<ExamPaperQuestion>()
                        .eq(ExamPaperQuestion::getPaperId, paperId)
                        .orderByAsc(ExamPaperQuestion::getSortOrder)
        );
        for (int i = 0; i < remaining.size(); i++) {
            remaining.get(i).setSortOrder(i + 1);
            paperQuestionMapper.updateById(remaining.get(i));
        }

        recalcTotalScore(paperId);
    }

    /**
     * 重新计算试卷总分
     */
    private void recalcTotalScore(Long paperId) {
        List<ExamPaperQuestion> pqList = paperQuestionMapper.selectList(
                new LambdaQueryWrapper<ExamPaperQuestion>()
                        .eq(ExamPaperQuestion::getPaperId, paperId)
        );
        BigDecimal total = pqList.stream()
                .map(ExamPaperQuestion::getScore)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        ExamPaper paper = paperMapper.selectById(paperId);
        if (paper != null) {
            paper.setTotalScore(total);
            paperMapper.updateById(paper);
        }
    }

    /**
     * 设置试卷关联班级
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignClasses(Long paperId, List<Long> classIds) {
        // 先删除旧关联
        paperClassMapper.delete(
                new LambdaQueryWrapper<ExamPaperClass>().eq(ExamPaperClass::getPaperId, paperId)
        );
        // 添加新关联
        if (classIds != null && !classIds.isEmpty()) {
            for (Long classId : classIds) {
                ExamPaperClass pc = new ExamPaperClass();
                pc.setPaperId(paperId);
                pc.setClassId(classId);
                paperClassMapper.insert(pc);
            }
        }
    }

    /**
     * 获取试卷关联的班级ID列表
     */
    public List<Long> getPaperClassIds(Long paperId) {
        return paperClassMapper.selectList(
                new LambdaQueryWrapper<ExamPaperClass>().eq(ExamPaperClass::getPaperId, paperId)
        ).stream().map(ExamPaperClass::getClassId).collect(Collectors.toList());
    }

    /**
     * 校验学生是否有权限参加该试卷考试
     */
    public boolean canStudentAccessPaper(Long paperId, Long userId) {
        List<Long> paperClassIds = getPaperClassIds(paperId);
        if (paperClassIds.isEmpty()) return true; // 未指定班级则所有学生可访问
        List<Long> userClassIds = classService.getUserClassIds(userId);
        return paperClassIds.stream().anyMatch(userClassIds::contains);
    }
}
