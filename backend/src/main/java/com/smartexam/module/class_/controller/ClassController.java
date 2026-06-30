package com.smartexam.module.class_.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartexam.common.result.ResultVO;
import com.smartexam.module.class_.entity.SysClass;
import com.smartexam.module.class_.service.ClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "班级管理", description = "班级管理（管理员）")
@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
public class ClassController {

    private final ClassService classService;

    @Operation(summary = "分页查询班级")
    @GetMapping
    public ResultVO<Page<SysClass>> page(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResultVO.success(classService.page(keyword, pageNum, pageSize));
    }

    @Operation(summary = "获取所有班级（下拉选择用）")
    @GetMapping("/all")
    public ResultVO<List<SysClass>> listAll() {
        return ResultVO.success(classService.listAll());
    }

    @Operation(summary = "创建班级")
    @PostMapping
    public ResultVO<Long> create(@RequestBody SysClass cls) {
        return ResultVO.success(classService.create(cls));
    }

    @Operation(summary = "更新班级")
    @PutMapping("/{id}")
    public ResultVO<Void> update(@PathVariable Long id, @RequestBody SysClass cls) {
        cls.setId(id);
        classService.update(cls);
        return ResultVO.success();
    }

    @Operation(summary = "删除班级")
    @DeleteMapping("/{id}")
    public ResultVO<Void> delete(@PathVariable Long id) {
        classService.delete(id);
        return ResultVO.success();
    }

    @Operation(summary = "获取班级成员")
    @GetMapping("/{id}/users")
    public ResultVO<List<Map<String, Object>>> getClassUsers(
            @PathVariable Long id,
            @RequestParam(required = false) String role) {
        return ResultVO.success(classService.getClassUsers(id, role));
    }

    @Operation(summary = "添加成员到班级")
    @PostMapping("/{id}/users")
    public ResultVO<Void> addClassUser(@PathVariable Long id, @RequestBody Map<String, Long> body) {
        classService.addClassUser(id, body.get("userId"));
        return ResultVO.success();
    }

    @Operation(summary = "从班级移除成员")
    @DeleteMapping("/{id}/users/{userId}")
    public ResultVO<Void> removeClassUser(@PathVariable Long id, @PathVariable Long userId) {
        classService.removeClassUser(id, userId);
        return ResultVO.success();
    }

    @Operation(summary = "获取可添加的用户列表")
    @GetMapping("/available-users")
    public ResultVO<List<Map<String, Object>>> getAvailableUsers(
            @RequestParam(required = false) String role) {
        return ResultVO.success(classService.getAvailableUsers(role));
    }
}
