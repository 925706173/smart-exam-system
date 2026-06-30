package com.smartexam.module.exam.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 考试记录实体类
 */
@Data
@TableName("exam_record")
@Schema(description = "考试记录实体")
public class ExamRecord {

    @TableId(type = IdType.AUTO)
    @Schema(description = "记录ID")
    private Long id;

    @Schema(description = "试卷ID")
    private Long paperId;

    @Schema(description = "学生ID")
    private Long userId;

    @Schema(description = "第几次考试")
    private Integer attemptNo;

    @Schema(description = "状态: NOT_STARTED, IN_PROGRESS, SUBMITTED, GRADING, GRADED")
    private String status;

    @Schema(description = "总得分")
    private BigDecimal totalScore;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "提交时间")
    private LocalDateTime submitTime;

    @Schema(description = "乱序种子")
    private Long shuffleSeed;

    @Schema(description = "题目顺序")
    private String questionOrder;

    @Schema(description = "选项顺序")
    private String optionOrder;

    @Schema(description = "考试IP地址")
    private String ipAddress;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
