package com.smartexam.module.ai.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * AI阅卷日志实体类
 */
@Data
@TableName("ai_grading_log")
@Schema(description = "AI阅卷日志实体")
public class AiGradingLog {

    @TableId(type = IdType.AUTO)
    @Schema(description = "日志ID")
    private Long id;

    @Schema(description = "答题记录ID")
    private Long answerId;

    @Schema(description = "题目ID")
    private Long questionId;

    @Schema(description = "模型名称")
    private String modelName;

    @Schema(description = "发送的Prompt")
    private String prompt;

    @Schema(description = "AI返回结果")
    private String response;

    @Schema(description = "AI评分")
    private BigDecimal aiScore;

    @Schema(description = "AI评语")
    private String aiComment;

    @Schema(description = "置信度")
    private BigDecimal confidence;

    @Schema(description = "Token消耗量")
    private Integer tokenUsed;

    @Schema(description = "调用耗时(毫秒)")
    private Integer durationMs;

    @Schema(description = "状态: SUCCESS, FAILED, TIMEOUT, FALLBACK")
    private String status;

    @Schema(description = "错误信息")
    private String errorMsg;

    @Schema(description = "重试次数")
    private Integer retryCount;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
