package com.smartexam.module.grading.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 评分请求DTO
 */
@Data
@Schema(description = "评分请求数据")
public class GradeAnswerDTO {

    @Schema(description = "最终分数")
    private BigDecimal finalScore;

    @Schema(description = "评语")
    private String comment;
}
