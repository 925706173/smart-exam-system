package com.smartexam.module.dict.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartexam.common.result.ResultVO;
import com.smartexam.module.dict.entity.SysDict;
import com.smartexam.module.dict.service.DictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典控制器
 */
@Tag(name = "字典模块", description = "字典管理（管理员）")
@RestController
@RequestMapping("/api/dicts")
@RequiredArgsConstructor
public class DictController {

    private final DictService dictService;

    @Operation(summary = "字典列表")
    @GetMapping
    public ResultVO<Page<SysDict>> page(
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Page<SysDict> page = dictService.page(type, pageNum, pageSize);
        return ResultVO.success(page);
    }

    @Operation(summary = "按类型查询字典")
    @GetMapping("/type/{type}")
    public ResultVO<List<SysDict>> getByType(@PathVariable String type) {
        List<SysDict> list = dictService.getByType(type);
        return ResultVO.success(list);
    }

    @Operation(summary = "创建字典")
    @PostMapping
    public ResultVO<Long> create(@RequestBody SysDict dict) {
        Long id = dictService.create(dict);
        return ResultVO.success(id);
    }

    @Operation(summary = "更新字典")
    @PutMapping("/{id}")
    public ResultVO<Void> update(@PathVariable Long id, @RequestBody SysDict dict) {
        dict.setId(id);
        dictService.update(dict);
        return ResultVO.success();
    }

    @Operation(summary = "删除字典")
    @DeleteMapping("/{id}")
    public ResultVO<Void> delete(@PathVariable Long id) {
        dictService.delete(id);
        return ResultVO.success();
    }
}
