package com.smartexam.module.class_.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_class")
@Schema(description = "班级实体")
public class SysClass {

    @TableId(type = IdType.AUTO)
    @Schema(description = "班级ID")
    private Long id;

    @Schema(description = "班级名称")
    private String name;

    @Schema(description = "班级描述")
    private String description;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @TableLogic
    @Schema(description = "逻辑删除")
    private Integer deleted;

    @TableField(exist = false)
    @Schema(description = "教师数量")
    private Integer teacherCount;

    @TableField(exist = false)
    @Schema(description = "学生数量")
    private Integer studentCount;
}
