package com.smartexam.module.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@TableName("sys_user")
@Schema(description = "用户实体")
public class SysUser {

    @TableId(type = IdType.AUTO)
    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户名(学号/工号)")
    private String username;

    @Schema(description = "密码(BCrypt加密)")
    private String password;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "角色: STUDENT-学生, TEACHER-教师, ADMIN-管理员")
    private String role;

    @Schema(description = "性别: 0-未知, 1-男, 2-女")
    private Integer gender;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "头像URL")
    private String avatar;

    @Schema(description = "班级ID")
    private Long classId;

    @Schema(description = "专业ID")
    private Long majorId;

    @Schema(description = "状态: 0-冻结, 1-正常")
    private Integer status;

    @Schema(description = "最后登录时间")
    private LocalDateTime lastLoginTime;

    @Schema(description = "最后登录IP")
    private String lastLoginIp;

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
