package com.smartexam.module.exam.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartexam.common.result.ResultVO;
import com.smartexam.module.exam.entity.ExamPaper;
import com.smartexam.module.exam.service.ExamPaperService;
import com.smartexam.module.user.entity.SysUser;
import com.smartexam.module.user.mapper.SysUserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 试卷管理控制器
 */
@Tag(name = "试卷模块", description = "试卷管理（教师）")
@RestController
@RequestMapping("/api/papers")
@RequiredArgsConstructor
public class ExamPaperController {

    private final ExamPaperService paperService;
    private final SysUserMapper userMapper;

    @Operation(summary = "分页查询试卷")
    @GetMapping
    public ResultVO<Page<ExamPaper>> page(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Long userId = StpUtil.getLoginIdAsLong();
        SysUser user = userMapper.selectById(userId);
        String role = user != null ? user.getRole() : null;
        Page<ExamPaper> page = paperService.page(keyword, status, pageNum, pageSize, userId, role);
        return ResultVO.success(page);
    }

    @Operation(summary = "获取试卷详情")
    @GetMapping("/{id}")
    public ResultVO<ExamPaper> getDetail(@PathVariable Long id) {
        ExamPaper paper = paperService.getDetail(id);
        return ResultVO.success(paper);
    }

    @Operation(summary = "创建试卷（手动）")
    @PostMapping
    public ResultVO<Long> create(@RequestBody ExamPaper paper) {
        Long id = paperService.create(paper);
        return ResultVO.success(id);
    }

    @Operation(summary = "规则抽题组卷")
    @PostMapping("/generate")
    public ResultVO<Long> generate(@RequestBody ExamPaper paper,
                                    @RequestParam String ruleConfig) {
        Long id = paperService.generate(paper, ruleConfig);
        return ResultVO.success(id);
    }

    @Operation(summary = "更新试卷")
    @PutMapping("/{id}")
    public ResultVO<Void> update(@PathVariable Long id, @RequestBody ExamPaper paper) {
        paper.setId(id);
        paperService.update(paper);
        return ResultVO.success();
    }

    @Operation(summary = "发布试卷")
    @PutMapping("/{id}/publish")
    public ResultVO<Void> publish(@PathVariable Long id) {
        paperService.publish(id);
        return ResultVO.success();
    }

    @Operation(summary = "删除试卷")
    @DeleteMapping("/{id}")
    public ResultVO<Void> delete(@PathVariable Long id) {
        paperService.delete(id);
        return ResultVO.success();
    }

    @Operation(summary = "获取试卷题目列表")
    @GetMapping("/{paperId}/questions")
    public ResultVO<List<Map<String, Object>>> getPaperQuestions(@PathVariable Long paperId) {
        return ResultVO.success(paperService.getPaperQuestions(paperId));
    }

    @Operation(summary = "向试卷添加题目")
    @PostMapping("/{paperId}/questions")
    public ResultVO<Void> addQuestion(@PathVariable Long paperId,
                                       @RequestParam Long questionId,
                                       @RequestParam BigDecimal score) {
        paperService.addQuestion(paperId, questionId, score);
        return ResultVO.success();
    }

    @Operation(summary = "更新试卷题目分值")
    @PutMapping("/{paperId}/questions/{pqId}")
    public ResultVO<Void> updatePaperQuestion(@PathVariable Long paperId,
                                               @PathVariable Long pqId,
                                               @RequestParam BigDecimal score) {
        paperService.updatePaperQuestion(paperId, pqId, score, null);
        return ResultVO.success();
    }

    @Operation(summary = "从试卷移除题目")
    @DeleteMapping("/{paperId}/questions/{pqId}")
    public ResultVO<Void> removeQuestion(@PathVariable Long paperId, @PathVariable Long pqId) {
        paperService.removeQuestion(paperId, pqId);
        return ResultVO.success();
    }

    @Operation(summary = "设置试卷关联班级")
    @PutMapping("/{id}/classes")
    public ResultVO<Void> assignClasses(@PathVariable Long id, @RequestBody Map<String, List<Long>> body) {
        paperService.assignClasses(id, body.get("classIds"));
        return ResultVO.success();
    }

    @Operation(summary = "获取试卷关联班级ID列表")
    @GetMapping("/{id}/classes")
    public ResultVO<List<Long>> getPaperClassIds(@PathVariable Long id) {
        return ResultVO.success(paperService.getPaperClassIds(id));
    }
}
