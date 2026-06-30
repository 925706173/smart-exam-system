package com.smartexam.module.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartexam.module.exam.entity.ExamAnswer;
import org.apache.ibatis.annotations.Mapper;

/**
 * 答题记录Mapper
 */
@Mapper
public interface ExamAnswerMapper extends BaseMapper<ExamAnswer> {
}
