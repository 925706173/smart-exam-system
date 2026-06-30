package com.smartexam.module.question.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 题目实体类
 */
@Data
@TableName("question")
@Schema(description = "题目实体")
public class Question {

    @TableId(type = IdType.AUTO)
    @Schema(description = "题目ID")
    private Long id;

    @Schema(description = "题干内容")
    private String title;

    @Schema(description = "题型: SINGLE-单选, MULTI-多选, JUDGE-判断, FILL-填空, SUBJECTIVE-主观")
    private String type;

    @Schema(description = "难度等级: 1-5")
    private Integer difficulty;

    @Schema(description = "科目ID")
    private Long subjectId;

    @Schema(description = "章节")
    private String chapter;

    @Schema(description = "默认分值")
    private BigDecimal score;

    @Schema(description = "参考答案")
    private String answer;

    @Schema(description = "答案解析")
    private String explanation;

    @Schema(description = "标签数组")
    private String tags;

    @Schema(description = "状态: DRAFT-草稿, PUBLISHED-已发布, DISABLED-已禁用")
    private String status;

    @Schema(description = "创建人ID")
    private Long createBy;

    @Schema(description = "更新人ID")
    private Long updateBy;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @TableLogic
    @Schema(description = "逻辑删除")
    private Integer deleted;

    /**
     * 选项列表（非数据库字段）
     */
    @TableField(exist = false)
    @Schema(description = "选项列表")
    private List<QuestionOption> options;
}
