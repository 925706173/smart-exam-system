package com.smartexam.module.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 组卷规则实体类
 */
@Data
@TableName("exam_rule")
@Schema(description = "组卷规则实体")
public class ExamRule {

    @TableId(type = IdType.AUTO)
    @Schema(description = "规则ID")
    private Long id;

    @Schema(description = "试卷ID")
    private Long paperId;

    @Schema(description = "规则配置JSON: [{\"chapter\":\"Ch1\",\"type\":\"SINGLE\",\"difficulty\":2,\"count\":5,\"score\":2}]")
    private String ruleConfig;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
