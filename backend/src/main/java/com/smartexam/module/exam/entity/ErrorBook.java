package com.smartexam.module.exam.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 错题本实体类
 */
@Data
@TableName("error_book")
@Schema(description = "错题本实体")
public class ErrorBook {

    @TableId(type = IdType.AUTO)
    @Schema(description = "错题ID")
    private Long id;

    @Schema(description = "学生ID")
    private Long userId;

    @Schema(description = "题目ID")
    private Long questionId;

    @Schema(description = "考试记录ID")
    private Long recordId;

    @Schema(description = "学生作答")
    private String userAnswer;

    @Schema(description = "正确答案")
    private String correctAnswer;

    @Schema(description = "错误次数")
    private Integer errorCount;

    @Schema(description = "最近错误时间")
    private LocalDateTime lastErrorTime;

    @Schema(description = "复习次数")
    private Integer reviewCount;

    @Schema(description = "最近复习时间")
    private LocalDateTime lastReviewTime;

    @Schema(description = "是否已掌握: 0-未掌握, 1-已掌握")
    private Integer mastered;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
