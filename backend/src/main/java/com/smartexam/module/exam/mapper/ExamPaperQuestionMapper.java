package com.smartexam.module.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartexam.module.exam.entity.ExamPaperQuestion;
import org.apache.ibatis.annotations.Mapper;

/**
 * 试卷题目关联Mapper
 */
@Mapper
public interface ExamPaperQuestionMapper extends BaseMapper<ExamPaperQuestion> {
}
