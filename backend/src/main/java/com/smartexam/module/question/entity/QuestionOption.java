package com.smartexam.module.question.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 选项实体类
 */
@Data
@TableName("question_option")
@Schema(description = "选项实体")
public class QuestionOption {

    @TableId(type = IdType.AUTO)
    @Schema(description = "选项ID")
    private Long id;

    @Schema(description = "题目ID")
    private Long questionId;

    @Schema(description = "选项标识: A, B, C, D...")
    private String optionLabel;

    @Schema(description = "选项内容")
    private String optionContent;

    @Schema(description = "是否正确答案: 0-否, 1-是")
    private Integer isCorrect;

    @Schema(description = "排序号")
    private Integer sortOrder;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 乱序后的正确标识（非数据库字段，用于前端显示）
     */
    @TableField(exist = false)
    @Schema(description = "原始正确标识")
    private String originCorrect;
}
