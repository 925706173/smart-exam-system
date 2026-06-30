package com.smartexam.module.question.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 题目查询DTO
 */
@Data
@Schema(description = "题目查询参数")
public class QuestionQueryDTO {

    @Schema(description = "题型")
    private String type;

    @Schema(description = "难度等级")
    private Integer difficulty;

    @Schema(description = "科目ID")
    private Long subjectId;

    @Schema(description = "章节")
    private String chapter;

    @Schema(description = "关键词")
    private String keyword;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "页码")
    private Integer pageNum = 1;

    @Schema(description = "每页数量")
    private Integer pageSize = 10;
}
