package com.smartexam.module.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartexam.common.result.ResultVO;
import com.smartexam.module.user.dto.UserQueryDTO;
import com.smartexam.module.user.entity.SysUser;
import com.smartexam.module.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 用户管理控制器
 */
@Tag(name = "用户模块", description = "用户管理（管理员）")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "分页查询用户列表")
    @GetMapping
    public ResultVO<Page<SysUser>> page(UserQueryDTO query) {
        Page<SysUser> page = userService.page(query);
        return ResultVO.success(page);
    }

    @Operation(summary = "创建用户")
    @PostMapping
    public ResultVO<Long> create(@RequestBody SysUser user) {
        Long id = userService.create(user);
        return ResultVO.success(id);
    }

    @Operation(summary = "更新用户")
    @PutMapping("/{id}")
    public ResultVO<Void> update(@PathVariable Long id, @RequestBody SysUser user) {
        user.setId(id);
        userService.update(user);
        return ResultVO.success();
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public ResultVO<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResultVO.success();
    }

    @Operation(summary = "Excel批量导入用户")
    @PostMapping("/import")
    public ResultVO<Map<String, Object>> importUsers(@RequestParam("file") MultipartFile file,
                                                      @RequestParam("role") String role) {
        Map<String, Object> result = userService.importUsers(file, role);
        return ResultVO.success(result);
    }

    @Operation(summary = "重置密码")
    @PutMapping("/{id}/reset-password")
    public ResultVO<Void> resetPassword(@PathVariable Long id) {
        userService.resetPassword(id);
        return ResultVO.success();
    }

    @Operation(summary = "冻结/解冻用户")
    @PutMapping("/{id}/toggle-status")
    public ResultVO<Void> toggleStatus(@PathVariable Long id) {
        userService.toggleStatus(id);
        return ResultVO.success();
    }

    @Operation(summary = "修改个人信息")
    @PutMapping("/profile")
    public ResultVO<Void> updateProfile(@RequestBody SysUser user) {
        userService.updateProfile(user);
        return ResultVO.success();
    }
}
