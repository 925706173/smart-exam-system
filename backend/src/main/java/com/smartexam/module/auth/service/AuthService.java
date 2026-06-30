package com.smartexam.module.auth.service;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smartexam.common.exception.BizException;
import com.smartexam.common.result.ResultCode;
import com.smartexam.module.auth.dto.LoginDTO;
import com.smartexam.module.user.entity.SysUser;
import com.smartexam.module.user.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证服务
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final SysUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * 用户登录
     */
    public Map<String, Object> login(LoginDTO dto) {
        // 查询用户
        SysUser user = userMapper.selectOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, dto.getUsername())
                        .eq(SysUser::getDeleted, 0)
        );

        if (user == null) {
            throw new BizException(ResultCode.LOGIN_FAILED);
        }

        // 校验密码
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BizException(ResultCode.LOGIN_FAILED);
        }

        // 校验状态
        if (user.getStatus() == 0) {
            throw new BizException(ResultCode.ACCOUNT_DISABLED);
        }

        // Sa-Token登录
        StpUtil.login(user.getId());
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        // 更新登录信息
        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateById(user);

        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", tokenInfo.getTokenValue());
        result.put("tokenName", tokenInfo.getTokenName());
        result.put("role", user.getRole());
        result.put("userId", user.getId());
        result.put("realName", user.getRealName());

        return result;
    }

    /**
     * 获取当前用户信息
     */
    public Map<String, Object> getUserInfo() {
        Long userId = StpUtil.getLoginIdAsLong();
        SysUser user = userMapper.selectById(userId);
        if (user == null) {
            throw new BizException(ResultCode.USER_NOT_FOUND);
        }

        Map<String, Object> info = new HashMap<>();
        info.put("id", user.getId());
        info.put("username", user.getUsername());
        info.put("realName", user.getRealName());
        info.put("role", user.getRole());
        info.put("gender", user.getGender());
        info.put("email", user.getEmail());
        info.put("phone", user.getPhone());
        info.put("avatar", user.getAvatar());
        info.put("classId", user.getClassId());
        info.put("majorId", user.getMajorId());

        return info;
    }
}
