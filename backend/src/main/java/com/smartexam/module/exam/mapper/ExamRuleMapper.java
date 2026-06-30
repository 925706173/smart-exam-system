package com.smartexam.module.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartexam.module.exam.entity.ExamRule;
import org.apache.ibatis.annotations.Mapper;

/**
 * 组卷规则Mapper
 */
@Mapper
public interface ExamRuleMapper extends BaseMapper<ExamRule> {
}
