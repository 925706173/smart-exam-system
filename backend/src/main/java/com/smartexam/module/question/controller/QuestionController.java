package com.smartexam.module.question.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartexam.common.result.ResultVO;
import com.smartexam.module.question.dto.QuestionQueryDTO;
import com.smartexam.module.question.entity.Question;
import com.smartexam.module.question.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 题库管理控制器
 */
@Tag(name = "题库模块", description = "题库管理（教师）")
@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @Operation(summary = "分页查询题目")
    @GetMapping
    public ResultVO<Page<Question>> page(QuestionQueryDTO query) {
        Page<Question> page = questionService.page(query);
        return ResultVO.success(page);
    }

    @Operation(summary = "获取题目详情")
    @GetMapping("/{id}")
    public ResultVO<Question> getDetail(@PathVariable Long id) {
        Question question = questionService.getDetail(id);
        return ResultVO.success(question);
    }

    @Operation(summary = "创建题目")
    @PostMapping
    public ResultVO<Long> create(@RequestBody Question question) {
        Long id = questionService.create(question);
        return ResultVO.success(id);
    }

    @Operation(summary = "更新题目")
    @PutMapping("/{id}")
    public ResultVO<Void> update(@PathVariable Long id, @RequestBody Question question) {
        question.setId(id);
        questionService.update(question);
        return ResultVO.success();
    }

    @Operation(summary = "删除题目")
    @DeleteMapping("/{id}")
    public ResultVO<Void> delete(@PathVariable Long id) {
        questionService.delete(id);
        return ResultVO.success();
    }

    @Operation(summary = "Excel批量导入题库")
    @PostMapping("/import")
    public ResultVO<Map<String, Object>> importQuestions(
            @RequestParam("file") MultipartFile file,
            @RequestParam("subjectId") Long subjectId) {
        Map<String, Object> result = questionService.importQuestions(file, subjectId);
        return ResultVO.success(result);
    }
}
