package com.smartexam.module.exam.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 练习判分结果VO
 */
@Data
@Schema(description = "练习判分结果")
public class PracticeCheckVO {

    @Schema(description = "是否正确")
    private Boolean isCorrect;

    @Schema(description = "正确答案")
    private String correctAnswer;

    @Schema(description = "答案解析")
    private String explanation;
}
