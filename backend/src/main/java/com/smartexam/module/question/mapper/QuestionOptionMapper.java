package com.smartexam.module.question.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartexam.module.question.entity.QuestionOption;
import org.apache.ibatis.annotations.Mapper;

/**
 * 选项Mapper
 */
@Mapper
public interface QuestionOptionMapper extends BaseMapper<QuestionOption> {
}
