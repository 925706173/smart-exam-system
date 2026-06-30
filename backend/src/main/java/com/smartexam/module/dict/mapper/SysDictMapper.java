package com.smartexam.module.dict.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartexam.module.dict.entity.SysDict;
import org.apache.ibatis.annotations.Mapper;

/**
 * 字典Mapper
 */
@Mapper
public interface SysDictMapper extends BaseMapper<SysDict> {
}
