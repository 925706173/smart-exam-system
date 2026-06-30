package com.smartexam.module.exam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 考试开始返回DTO
 */
@Data
@Schema(description = "考试开始返回数据")
public class ExamStartDTO {

    @Schema(description = "考试记录ID")
    private Long recordId;

    @Schema(description = "试卷标题")
    private String paperTitle;

    @Schema(description = "考试时长(分钟)")
    private Integer duration;

    @Schema(description = "试卷总分")
    private BigDecimal totalScore;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "题目数量")
    private Integer questionCount;
}
