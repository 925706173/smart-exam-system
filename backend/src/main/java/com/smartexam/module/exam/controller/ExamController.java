package com.smartexam.module.exam.controller;

import com.smartexam.common.result.ResultVO;
import com.smartexam.module.exam.dto.DraftDTO;
import com.smartexam.module.exam.dto.ExamStartDTO;
import com.smartexam.module.exam.service.ExamService;
import com.smartexam.module.grading.vo.GradingPaperDetailVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 考试控制器
 */
@Tag(name = "考试模块", description = "在线考试（学生）")
@RestController
@RequestMapping("/api/exam")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @Operation(summary = "开始考试")
    @PostMapping("/start/{paperId}")
    public ResultVO<ExamStartDTO> startExam(@PathVariable Long paperId) {
        ExamStartDTO result = examService.startExam(paperId);
        return ResultVO.success(result);
    }

    @Operation(summary = "获取题目ID列表（答题卡）")
    @GetMapping("/records/{id}/question-ids")
    public ResultVO<Map<String, Object>> getQuestionIds(@PathVariable Long id) {
        Map<String, Object> result = examService.getQuestionIds(id);
        return ResultVO.success(result);
    }

    @Operation(summary = "获取试卷题目（分页）")
    @GetMapping("/records/{id}/questions")
    public ResultVO<Map<String, Object>> getQuestions(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> result = examService.getQuestions(id, page, size);
        return ResultVO.success(result);
    }

    @Operation(summary = "保存草稿")
    @PostMapping("/draft")
    public ResultVO<Void> saveDraft(@Valid @RequestBody DraftDTO dto) {
        examService.saveDraft(dto.getRecordId(), dto.getAnswers());
        return ResultVO.success();
    }

    @Operation(summary = "获取草稿")
    @GetMapping("/draft/{recordId}")
    public ResultVO<Map<Long, String>> getDraft(@PathVariable Long recordId) {
        Map<Long, String> draft = examService.getDraft(recordId);
        return ResultVO.success(draft);
    }

    @Operation(summary = "提交单题答案")
    @PostMapping("/records/{id}/answer")
    public ResultVO<Void> submitAnswer(
            @PathVariable Long id,
            @RequestParam Long questionId,
            @RequestParam String answer) {
        examService.submitAnswer(id, questionId, answer);
        return ResultVO.success();
    }

    @Operation(summary = "交卷")
    @PostMapping("/records/{id}/submit")
    public ResultVO<Void> submitExam(@PathVariable Long id) {
        examService.submitExam(id);
        return ResultVO.success();
    }

    @Operation(summary = "获取已批改试卷列表（试卷复查）")
    @GetMapping("/review/list")
    public ResultVO<List<Map<String, Object>>> getReviewList() {
        return ResultVO.success(examService.getReviewList());
    }

    @Operation(summary = "获取试卷复查详情")
    @GetMapping("/review/{recordId}")
    public ResultVO<GradingPaperDetailVO> getReviewDetail(@PathVariable Long recordId) {
        return ResultVO.success(examService.getReviewDetail(recordId));
    }

    @Operation(summary = "重新判分客观题（修复判分错误）")
    @PostMapping("/records/{id}/regrade")
    public ResultVO<Void> reGradeObjectiveAnswers(@PathVariable Long id) {
        examService.reGradeObjectiveAnswers(id);
        return ResultVO.success();
    }
}
