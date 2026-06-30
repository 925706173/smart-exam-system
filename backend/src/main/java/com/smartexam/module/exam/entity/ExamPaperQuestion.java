package com.smartexam.module.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 试卷题目关联实体类
 */
@Data
@TableName("exam_paper_question")
@Schema(description = "试卷题目关联实体")
public class ExamPaperQuestion {

    @TableId(type = IdType.AUTO)
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "试卷ID")
    private Long paperId;

    @Schema(description = "题目ID")
    private Long questionId;

    @Schema(description = "该题分值")
    private BigDecimal score;

    @Schema(description = "题目排序")
    private Integer sortOrder;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
