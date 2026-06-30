package com.smartexam.module.stats.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 试卷报告VO
 */
@Data
@Schema(description = "试卷成绩报告")
public class PaperReportVO {

    @Schema(description = "试卷ID")
    private Long paperId;

    @Schema(description = "试卷标题")
    private String paperTitle;

    @Schema(description = "总分")
    private BigDecimal totalScore;

    @Schema(description = "及格分数")
    private BigDecimal passScore;

    @Schema(description = "参考人数")
    private Integer totalCount;

    @Schema(description = "及格人数")
    private Integer passCount;

    @Schema(description = "不及格人数")
    private Integer failCount;

    @Schema(description = "及格率")
    private BigDecimal passRate;

    @Schema(description = "平均分")
    private BigDecimal avgScore;

    @Schema(description = "最高分")
    private BigDecimal maxScore;

    @Schema(description = "最低分")
    private BigDecimal minScore;

    @Schema(description = "分数段分布: [{label:\"0-59\", count:10}, ...]")
    private List<Map<String, Object>> scoreDistribution;
}
