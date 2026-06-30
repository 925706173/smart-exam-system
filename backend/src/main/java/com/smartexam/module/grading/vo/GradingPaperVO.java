package com.smartexam.module.grading.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 阅卷试卷VO
 */
@Data
@Schema(description = "阅卷试卷信息")
public class GradingPaperVO {

    @Schema(description = "试卷ID")
    private Long paperId;

    @Schema(description = "试卷标题")
    private String paperTitle;

    @Schema(description = "科目名称")
    private String subjectName;

    @Schema(description = "提交人数")
    private Integer submitCount;

    @Schema(description = "待阅卷人数")
    private Integer pendingCount;

    @Schema(description = "已阅卷人数")
    private Integer gradedCount;

    @Schema(description = "平均分")
    private BigDecimal avgScore;

    @Schema(description = "提交时间")
    private LocalDateTime submitTime;
}
