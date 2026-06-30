package com.smartexam.module.question.service;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartexam.common.exception.BizException;
import com.smartexam.common.result.ResultCode;
import com.smartexam.module.question.dto.QuestionImportDTO;
import com.smartexam.module.question.dto.QuestionQueryDTO;
import com.smartexam.module.question.entity.Question;
import com.smartexam.module.question.entity.QuestionOption;
import com.smartexam.module.question.listener.QuestionImportListener;
import com.smartexam.module.question.mapper.QuestionMapper;
import com.smartexam.module.question.mapper.QuestionOptionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 题库服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionMapper questionMapper;
    private final QuestionOptionMapper questionOptionMapper;

    /**
     * 分页查询题目
     */
    public Page<Question> page(QuestionQueryDTO query) {
        Page<Question> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();

        if (query.getType() != null && !query.getType().isEmpty()) {
            wrapper.eq(Question::getType, query.getType());
        }
        if (query.getDifficulty() != null) {
            wrapper.eq(Question::getDifficulty, query.getDifficulty());
        }
        if (query.getSubjectId() != null) {
            wrapper.eq(Question::getSubjectId, query.getSubjectId());
        }
        if (query.getChapter() != null && !query.getChapter().isEmpty()) {
            wrapper.like(Question::getChapter, query.getChapter());
        }
        if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
            wrapper.and(w -> w.like(Question::getTitle, query.getKeyword())
                    .or().like(Question::getAnswer, query.getKeyword()));
        }
        if (query.getStatus() != null && !query.getStatus().isEmpty()) {
            wrapper.eq(Question::getStatus, query.getStatus());
        }

        wrapper.orderByDesc(Question::getCreateTime);
        return questionMapper.selectPage(page, wrapper);
    }

    /**
     * 获取题目详情（含选项）
     */
    public Question getDetail(Long id) {
        Question question = questionMapper.selectById(id);
        if (question == null) {
            throw new BizException(ResultCode.QUESTION_NOT_FOUND);
        }
        // 查询选项
        List<QuestionOption> options = questionOptionMapper.selectList(
                new LambdaQueryWrapper<QuestionOption>()
                        .eq(QuestionOption::getQuestionId, id)
                        .orderByAsc(QuestionOption::getSortOrder)
        );
        question.setOptions(options);
        return question;
    }

    /**
     * 创建题目
     */
    @Transactional(rollbackFor = Exception.class)
    public Long create(Question question) {
        Long userId = StpUtil.getLoginIdAsLong();
        question.setCreateBy(userId);
        question.setUpdateBy(userId);
        question.setStatus("DRAFT");

        // 暂存选项，先不保存到question的options字段
        List<QuestionOption> optionsToSave = question.getOptions();
        question.setOptions(null);
        questionMapper.insert(question);

        // 保存选项
        if (optionsToSave != null) {
            int sortOrder = 0;
            for (QuestionOption option : optionsToSave) {
                option.setQuestionId(question.getId());
                option.setSortOrder(sortOrder++);
                // 如果optionLabel为空，自动生成A/B/C/D
                if (option.getOptionLabel() == null || option.getOptionLabel().isEmpty()) {
                    option.setOptionLabel(String.valueOf((char) ('A' + sortOrder - 1)));
                }
                questionOptionMapper.insert(option);
            }
        }

        return question.getId();
    }

    /**
     * 更新题目
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(Question question) {
        Long userId = StpUtil.getLoginIdAsLong();
        question.setUpdateBy(userId);
        questionMapper.updateById(question);

        // 更新选项（先删后插）
        if (question.getOptions() != null) {
            questionOptionMapper.delete(
                    new LambdaQueryWrapper<QuestionOption>()
                            .eq(QuestionOption::getQuestionId, question.getId())
            );
            int sortOrder = 0;
            for (QuestionOption option : question.getOptions()) {
                option.setQuestionId(question.getId());
                option.setSortOrder(sortOrder++);
                questionOptionMapper.insert(option);
            }
        }
    }

    /**
     * 删除题目
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        questionMapper.deleteById(id);
        questionOptionMapper.delete(
                new LambdaQueryWrapper<QuestionOption>()
                        .eq(QuestionOption::getQuestionId, id)
        );
    }

    /**
     * Excel批量导入题库
     */
    public Map<String, Object> importQuestions(MultipartFile file, Long subjectId) {
        Long userId = StpUtil.getLoginIdAsLong();

        try {
            QuestionImportListener listener = new QuestionImportListener(
                    questionMapper, questionOptionMapper, subjectId, userId
            );

            EasyExcel.read(file.getInputStream(), QuestionImportDTO.class, listener)
                    .sheet()
                    .doRead();

            QuestionImportListener.ImportResult result = listener.getResult();

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("successCount", result.getSuccessCount());
            resultMap.put("failCount", result.getFailCount());
            resultMap.put("errorDetails", result.getErrorDetails());
            return resultMap;

        } catch (IOException e) {
            log.error("文件读取失败", e);
            throw new BizException(ResultCode.IMPORT_FORMAT_ERROR);
        }
    }
}
