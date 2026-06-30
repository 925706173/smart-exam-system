package com.smartexam.module.grading.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 批量评分DTO
 */
@Data
@Schema(description = "批量评分请求")
public class BatchGradeDTO {

    @Schema(description = "评分项列表")
    private List<GradeItem> items;

    @Data
    @Schema(description = "单题评分项")
    public static class GradeItem {

        @Schema(description = "答题记录ID")
        private Long answerId;

        @Schema(description = "最终分数")
        private BigDecimal finalScore;

        @Schema(description = "评语（可选）")
        private String comment;
    }
}
