package com.smartexam.module.exam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/**
 * 考试提交DTO
 */
@Data
@Schema(description = "考试提交数据")
public class ExamSubmitDTO {

    @Schema(description = "答案Map: {questionId: answer}")
    private Map<Long, String> answers;
}
