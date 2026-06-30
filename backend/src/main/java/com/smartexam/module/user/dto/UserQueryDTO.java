package com.smartexam.module.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户查询DTO
 */
@Data
@Schema(description = "用户查询参数")
public class UserQueryDTO {

    @Schema(description = "角色")
    private String role;

    @Schema(description = "关键词(用户名/姓名)")
    private String keyword;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "班级ID")
    private Long classId;

    @Schema(description = "专业ID")
    private Long majorId;

    @Schema(description = "页码")
    private Integer pageNum = 1;

    @Schema(description = "每页数量")
    private Integer pageSize = 10;
}
