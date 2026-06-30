package com.smartexam.module.exam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * 草稿保存DTO
 */
@Data
@Schema(description = "草稿保存数据")
public class DraftDTO {

    @NotNull(message = "考试记录ID不能为空")
    @Schema(description = "考试记录ID")
    private Long recordId;

    @Schema(description = "答案Map: {questionId: answer}")
    private Map<Long, String> answers;
}
