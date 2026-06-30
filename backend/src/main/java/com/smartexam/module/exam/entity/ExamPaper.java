package com.smartexam.module.exam.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 试卷实体类
 */
@Data
@TableName("exam_paper")
@Schema(description = "试卷实体")
public class ExamPaper {

    @TableId(type = IdType.AUTO)
    @Schema(description = "试卷ID")
    private Long id;

    @Schema(description = "试卷标题")
    private String title;

    @Schema(description = "试卷说明")
    private String description;

    @Schema(description = "科目ID")
    private Long subjectId;

    @Schema(description = "科目名称")
    private String subjectName;

    @Schema(description = "学期ID")
    private Long semesterId;

    @Schema(description = "试卷总分")
    private BigDecimal totalScore;

    @Schema(description = "及格分数")
    private BigDecimal passScore;

    @Schema(description = "考试时长(分钟)")
    private Integer duration;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "是否乱序题目: 0-否, 1-是")
    private Integer shuffleQuestion;

    @Schema(description = "是否乱序选项: 0-否, 1-是")
    private Integer shuffleOption;

    @Schema(description = "交卷后是否显示答案: 0-否, 1-是")
    private Integer showAnswer;

    @Schema(description = "最大考试次数")
    private Integer maxAttempts;

    @Schema(description = "状态: DRAFT-草稿, PUBLISHED-已发布, CLOSED-已结束")
    private String status;

    @Schema(description = "类型: EXAM-考试, PRACTICE-练习")
    private String type;

    @Schema(description = "创建人ID")
    private Long createBy;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @TableLogic
    @Schema(description = "逻辑删除")
    private Integer deleted;
}
