package com.smartexam.module.dict.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 字典实体类
 */
@Data
@TableName("sys_dict")
@Schema(description = "字典实体")
public class SysDict {

    @TableId(type = IdType.AUTO)
    @Schema(description = "字典ID")
    private Long id;

    @Schema(description = "字典类型: CLASS-班级, MAJOR-专业, SUBJECT-科目, SEMESTER-学期")
    private String type;

    @Schema(description = "字典编码")
    private String code;

    @Schema(description = "字典名称")
    private String name;

    @Schema(description = "排序号")
    private Integer sortOrder;

    @Schema(description = "状态: 0-禁用, 1-启用")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @TableLogic
    @Schema(description = "逻辑删除: 0-未删除, 1-已删除")
    private Integer deleted;
}
