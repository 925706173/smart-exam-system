package com.smartexam.module.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartexam.module.exam.entity.ExamRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 考试记录Mapper
 */
@Mapper
public interface ExamRecordMapper extends BaseMapper<ExamRecord> {
}
