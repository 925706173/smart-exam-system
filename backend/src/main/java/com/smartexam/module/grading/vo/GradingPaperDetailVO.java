package com.smartexam.module.grading.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 答卷详情VO（教师批改用）
 */
@Data
@Schema(description = "答卷详情")
public class GradingPaperDetailVO {

    @Schema(description = "考试记录ID")
    private Long recordId;

    @Schema(description = "学生ID")
    private Long userId;

    @Schema(description = "学生姓名")
    private String studentName;

    @Schema(description = "试卷ID")
    private Long paperId;

    @Schema(description = "试卷标题")
    private String paperTitle;

    @Schema(description = "试卷总分")
    private BigDecimal totalScore;

    @Schema(description = "学生得分")
    private BigDecimal studentScore;

    @Schema(description = "考试状态")
    private String status;

    @Schema(description = "提交时间")
    private LocalDateTime submitTime;

    @Schema(description = "题目+答案列表")
    private List<GradingQuestionItem> questions;
}
