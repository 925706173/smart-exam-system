package com.smartexam.module.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartexam.module.exam.entity.ExamPaper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 试卷Mapper
 */
@Mapper
public interface ExamPaperMapper extends BaseMapper<ExamPaper> {
}
