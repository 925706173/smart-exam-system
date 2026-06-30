package com.smartexam.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 统一错误码枚举
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    // 通用
    SUCCESS(200, "操作成功"),
    FAILURE(500, "操作失败"),
    BAD_REQUEST(400, "参数错误"),
    UNAUTHORIZED(401, "未登录或Token已过期"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "资源不存在"),

    // 认证模块 1001-1999
    LOGIN_FAILED(1001, "用户名或密码错误"),
    ACCOUNT_DISABLED(1002, "账号已被冻结"),
    TOKEN_INVALID(1003, "Token无效"),
    TOKEN_EXPIRED(1004, "Token已过期"),
    CAPTCHA_ERROR(1005, "验证码错误"),

    // 用户模块 2001-2999
    USER_EXISTS(2001, "用户名已存在"),
    USER_NOT_FOUND(2002, "用户不存在"),
    PASSWORD_ERROR(2003, "密码错误"),
    IMPORT_FORMAT_ERROR(2004, "导入文件格式错误"),

    // 题库模块 3001-3999
    QUESTION_NOT_FOUND(3001, "题目不存在"),
    QUESTION_IMPORT_ERROR(3002, "题目导入失败"),
    QUESTION_TYPE_ERROR(3003, "题型不合法"),
    QUESTION_DIFFICULTY_ERROR(3004, "难度值不合法"),

    // 考试模块 4001-4999
    PAPER_NOT_FOUND(4001, "试卷不存在"),
    PAPER_NOT_PUBLISHED(4002, "试卷未发布"),
    PAPER_NOT_STARTED(4003, "考试未开始"),
    PAPER_ENDED(4004, "考试已结束"),
    RECORD_NOT_FOUND(4005, "考试记录不存在"),
    RECORD_ALREADY_SUBMITTED(4006, "已经交卷"),
    RECORD_NOT_IN_PROGRESS(4007, "考试未在进行中"),
    ATTEMPT_LIMIT_EXCEEDED(4008, "已达最大考试次数"),
    ANSWER_SUBMIT_FAILED(4009, "答案提交失败"),

    // 阅卷模块 5001-5999
    GRADING_FAILED(5001, "阅卷失败"),
    SCORE_INVALID(5002, "分数不合法"),
    ANSWER_NOT_FOUND(5003, "答题记录不存在"),

    // AI模块 6001-6999
    AI_CALL_FAILED(6001, "AI调用失败"),
    AI_TIMEOUT(6002, "AI调用超时"),
    AI_RESPONSE_PARSE_ERROR(6003, "AI返回解析失败"),
    AI_SCORE_INVALID(6004, "AI评分超出范围");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 消息
     */
    private final String message;
}
