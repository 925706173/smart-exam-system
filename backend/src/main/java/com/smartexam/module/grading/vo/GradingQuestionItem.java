package com.smartexam.module.grading.vo;

import com.smartexam.module.question.entity.QuestionOption;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 阅卷题目项VO（题目+学生答案+评分信息）
 */
@Data
@Schema(description = "阅卷题目项")
public class GradingQuestionItem {

    @Schema(description = "答题记录ID")
    private Long answerId;

    @Schema(description = "题目ID")
    private Long questionId;

    @Schema(description = "题干")
    private String title;

    @Schema(description = "题型: SINGLE, MULTI, JUDGE, FILL, SUBJECTIVE")
    private String type;

    @Schema(description = "满分")
    private BigDecimal fullScore;

    @Schema(description = "选项列表（选择题用）")
    private List<QuestionOption> options;

    @Schema(description = "学生答案")
    private String userAnswer;

    @Schema(description = "参考答案")
    private String referenceAnswer;

    @Schema(description = "答案解析")
    private String explanation;

    @Schema(description = "自动判分得分（客观题）")
    private BigDecimal autoScore;

    @Schema(description = "AI评分")
    private BigDecimal aiScore;

    @Schema(description = "AI评语")
    private String aiComment;

    @Schema(description = "最终分数")
    private BigDecimal finalScore;

    @Schema(description = "阅卷状态: PENDING, AI_DONE, TEACHER_DONE")
    private String gradingStatus;

    @Schema(description = "是否正确: 0-错误, 1-正确, NULL-未判")
    private Integer isCorrect;
}
