package com.smartexam.module.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartexam.module.user.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
}
