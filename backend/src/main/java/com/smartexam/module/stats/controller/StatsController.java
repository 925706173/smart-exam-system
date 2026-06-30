package com.smartexam.module.stats.controller;

import com.smartexam.common.result.ResultVO;
import com.smartexam.module.stats.service.StatsService;
import com.smartexam.module.stats.vo.PaperReportVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 统计控制器
 */
@Tag(name = "统计模块", description = "数据统计（教师/学生）")
@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @Operation(summary = "获取试卷成绩报告")
    @GetMapping("/paper/{id}/report")
    public ResultVO<PaperReportVO> getPaperReport(@PathVariable Long id) {
        PaperReportVO report = statsService.getPaperReport(id);
        return ResultVO.success(report);
    }

    @Operation(summary = "获取成绩排名")
    @GetMapping("/paper/{id}/rank")
    public ResultVO<List<Map<String, Object>>> getPaperRank(@PathVariable Long id) {
        List<Map<String, Object>> rank = statsService.getPaperRank(id);
        return ResultVO.success(rank);
    }

    @Operation(summary = "获取学生历史成绩")
    @GetMapping("/student/history")
    public ResultVO<List<Map<String, Object>>> getStudentHistory() {
        List<Map<String, Object>> history = statsService.getStudentHistory();
        return ResultVO.success(history);
    }
}
