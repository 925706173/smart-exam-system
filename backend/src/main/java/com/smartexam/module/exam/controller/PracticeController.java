package com.smartexam.module.exam.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartexam.common.result.ResultVO;
import com.smartexam.module.exam.entity.ErrorBook;
import com.smartexam.module.exam.mapper.ErrorBookMapper;
import com.smartexam.module.exam.service.PracticeService;
import com.smartexam.module.exam.vo.PracticeCheckVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 练习控制器
 */
@Tag(name = "练习模块", description = "练习模式（学生）")
@RestController
@RequestMapping("/api/practice")
@RequiredArgsConstructor
public class PracticeController {

    private final PracticeService practiceService;
    private final ErrorBookMapper errorBookMapper;

    @Operation(summary = "单题即时判分")
    @PostMapping("/check")
    public ResultVO<PracticeCheckVO> checkAnswer(
            @RequestParam Long questionId,
            @RequestParam String answer) {
        PracticeCheckVO result = practiceService.checkAnswer(questionId, answer);
        return ResultVO.success(result);
    }

    @Operation(summary = "错题本列表")
    @GetMapping("/error-book")
    public ResultVO<Page<ErrorBook>> getErrorBook(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Page<ErrorBook> page = practiceService.getErrorBook(pageNum, pageSize);
        return ResultVO.success(page);
    }

    @Operation(summary = "错题复习")
    @PostMapping("/review/{id}")
    public ResultVO<Void> reviewError(@PathVariable Long id) {
        practiceService.reviewError(id);
        return ResultVO.success();
    }
}
