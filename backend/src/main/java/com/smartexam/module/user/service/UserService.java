package com.smartexam.module.user.service;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartexam.common.exception.BizException;
import com.smartexam.common.result.ResultCode;
import com.smartexam.module.user.dto.UserQueryDTO;
import com.smartexam.module.user.entity.SysUser;
import com.smartexam.module.user.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final SysUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    private static final String DEFAULT_PASSWORD = "123456";

    /**
     * 分页查询用户
     */
    public Page<SysUser> page(UserQueryDTO query) {
        Page<SysUser> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();

        if (query.getRole() != null && !query.getRole().isEmpty()) {
            wrapper.eq(SysUser::getRole, query.getRole());
        }
        if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
            wrapper.and(w -> w.like(SysUser::getUsername, query.getKeyword())
                    .or().like(SysUser::getRealName, query.getKeyword()));
        }
        if (query.getStatus() != null) {
            wrapper.eq(SysUser::getStatus, query.getStatus());
        }
        if (query.getClassId() != null) {
            wrapper.eq(SysUser::getClassId, query.getClassId());
        }
        if (query.getMajorId() != null) {
            wrapper.eq(SysUser::getMajorId, query.getMajorId());
        }

        wrapper.orderByAsc(SysUser::getId);
        return userMapper.selectPage(page, wrapper);
    }

    /**
     * 创建用户
     */
    public Long create(SysUser user) {
        // 检查用户名是否已存在
        Long count = userMapper.selectCount(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, user.getUsername())
        );
        if (count > 0) {
            throw new BizException(ResultCode.USER_EXISTS);
        }

        // 加密密码
        user.setPassword(passwordEncoder.encode(
                user.getPassword() != null ? user.getPassword() : DEFAULT_PASSWORD
        ));
        user.setStatus(1);
        userMapper.insert(user);
        return user.getId();
    }

    /**
     * 更新用户
     */
    public void update(SysUser user) {
        SysUser existUser = userMapper.selectById(user.getId());
        if (existUser == null) {
            throw new BizException(ResultCode.USER_NOT_FOUND);
        }

        // 不允许修改密码字段
        user.setPassword(null);
        userMapper.updateById(user);
    }

    /**
     * 删除用户
     */
    public void delete(Long id) {
        userMapper.deleteById(id);
    }

    /**
     * 重置密码
     */
    public void resetPassword(Long id) {
        SysUser user = userMapper.selectById(id);
        if (user == null) {
            throw new BizException(ResultCode.USER_NOT_FOUND);
        }
        user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        userMapper.updateById(user);
    }

    /**
     * 冻结/解冻用户
     */
    public void toggleStatus(Long id) {
        SysUser user = userMapper.selectById(id);
        if (user == null) {
            throw new BizException(ResultCode.USER_NOT_FOUND);
        }
        user.setStatus(user.getStatus() == 1 ? 0 : 1);
        userMapper.updateById(user);
    }

    /**
     * 修改个人信息
     */
    public void updateProfile(SysUser user) {
        Long userId = StpUtil.getLoginIdAsLong();
        user.setId(userId);
        user.setPassword(null);
        user.setRole(null);
        user.setStatus(null);
        userMapper.updateById(user);
    }

    /**
     * Excel批量导入用户
     */
    public Map<String, Object> importUsers(MultipartFile file, String role) {
        // TODO: 实现EasyExcel导入逻辑
        Map<String, Object> result = new HashMap<>();
        result.put("successCount", 0);
        result.put("failCount", 0);
        result.put("errorDetails", new java.util.ArrayList<>());
        return result;
    }
}
