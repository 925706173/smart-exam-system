package com.smartexam.module.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartexam.module.ai.entity.AiGradingLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * AI阅卷日志Mapper
 */
@Mapper
public interface AiGradingLogMapper extends BaseMapper<AiGradingLog> {
}
