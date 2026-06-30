package com.smartexam.module.exam.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 答题记录实体类
 */
@Data
@TableName("exam_answer")
@Schema(description = "答题记录实体")
public class ExamAnswer {

    @TableId(type = IdType.AUTO)
    @Schema(description = "答题ID")
    private Long id;

    @Schema(description = "考试记录ID")
    private Long recordId;

    @Schema(description = "题目ID")
    private Long questionId;

    @Schema(description = "学生答案")
    private String userAnswer;

    @Schema(description = "得分")
    private BigDecimal score;

    @Schema(description = "是否正确: 0-错误, 1-正确, NULL-未判")
    private Integer isCorrect;

    @Schema(description = "AI评分")
    private BigDecimal aiScore;

    @Schema(description = "AI评语")
    private String aiComment;

    @Schema(description = "最终分数")
    private BigDecimal finalScore;

    @Schema(description = "阅卷状态: PENDING, AI_DONE, TEACHER_DONE")
    private String gradingStatus;

    @Schema(description = "答题耗时(秒)")
    private Integer answerTime;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
