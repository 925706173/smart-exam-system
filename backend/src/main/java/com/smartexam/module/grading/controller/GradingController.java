package com.smartexam.module.grading.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartexam.common.result.ResultVO;
import com.smartexam.module.grading.dto.BatchGradeDTO;
import com.smartexam.module.grading.dto.GradeAnswerDTO;
import com.smartexam.module.grading.service.GradingService;
import com.smartexam.module.grading.vo.GradingPaperDetailVO;
import com.smartexam.module.grading.vo.GradingPaperVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 阅卷控制器
 */
@Tag(name = "阅卷模块", description = "阅卷管理（教师）")
@RestController
@RequestMapping("/api/grading")
@RequiredArgsConstructor
public class GradingController {

    private final GradingService gradingService;

    @Operation(summary = "获取待阅卷试卷列表")
    @GetMapping("/papers")
    public ResultVO<Page<GradingPaperVO>> getGradingPapers(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Page<GradingPaperVO> page = gradingService.getGradingPapers(pageNum, pageSize);
        return ResultVO.success(page);
    }

    @Operation(summary = "获取试卷答题列表")
    @GetMapping("/papers/{id}/answers")
    public ResultVO<Map<String, Object>> getPaperAnswers(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> result = gradingService.getPaperAnswers(id, pageNum, pageSize);
        return ResultVO.success(result);
    }

    @Operation(summary = "人工评分/覆盖AI分")
    @PutMapping("/answers/{id}")
    public ResultVO<Void> gradeAnswer(@PathVariable Long id, @RequestBody GradeAnswerDTO dto) {
        gradingService.gradeAnswer(id, dto);
        return ResultVO.success();
    }

    @Operation(summary = "触发AI批量阅卷")
    @PostMapping("/papers/{id}/ai-grade")
    public ResultVO<Void> triggerAiGrade(@PathVariable Long id) {
        gradingService.triggerAiGrade(id);
        return ResultVO.success();
    }

    @Operation(summary = "获取试卷的学生提交列表")
    @GetMapping("/papers/{paperId}/students")
    public ResultVO<List<Map<String, Object>>> getPaperStudents(@PathVariable Long paperId) {
        List<Map<String, Object>> students = gradingService.getPaperStudents(paperId);
        return ResultVO.success(students);
    }

    @Operation(summary = "获取答卷详情（题目+答案）")
    @GetMapping("/papers/{paperId}/records/{recordId}/detail")
    public ResultVO<GradingPaperDetailVO> getRecordDetail(
            @PathVariable Long paperId,
            @PathVariable Long recordId) {
        GradingPaperDetailVO detail = gradingService.getRecordDetail(paperId, recordId);
        return ResultVO.success(detail);
    }

    @Operation(summary = "批量评分提交")
    @PostMapping("/records/{recordId}/batch-grade")
    public ResultVO<Void> batchGrade(
            @PathVariable Long recordId,
            @RequestBody BatchGradeDTO dto) {
        gradingService.batchGrade(recordId, dto);
        return ResultVO.success();
    }
}
