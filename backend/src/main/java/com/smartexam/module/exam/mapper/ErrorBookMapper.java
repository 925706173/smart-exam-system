package com.smartexam.module.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartexam.module.exam.entity.ErrorBook;
import org.apache.ibatis.annotations.Mapper;

/**
 * 错题本Mapper
 */
@Mapper
public interface ErrorBookMapper extends BaseMapper<ErrorBook> {
}
