package com.smartexam.module.auth.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.smartexam.common.result.ResultVO;
import com.smartexam.module.auth.dto.LoginDTO;
import com.smartexam.module.auth.service.AuthService;
import com.smartexam.module.user.entity.SysUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证控制器
 */
@Tag(name = "认证模块", description = "登录/登出/用户信息")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public ResultVO<Map<String, Object>> login(@Valid @RequestBody LoginDTO dto) {
        Map<String, Object> result = authService.login(dto);
        return ResultVO.success(result);
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public ResultVO<Void> logout() {
        StpUtil.logout();
        return ResultVO.success();
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/info")
    public ResultVO<Map<String, Object>> getUserInfo() {
        Map<String, Object> info = authService.getUserInfo();
        return ResultVO.success(info);
    }
}
