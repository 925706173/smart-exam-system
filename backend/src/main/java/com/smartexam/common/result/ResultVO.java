package com.smartexam.common.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应封装
 */
@Data
@Schema(description = "统一响应结果")
public class ResultVO<T> implements Serializable {

    @Schema(description = "状态码")
    private int code;

    @Schema(description = "响应消息")
    private String message;

    @Schema(description = "响应数据")
    private T data;

    @Schema(description = "时间戳")
    private long timestamp;

    public ResultVO() {
        this.timestamp = System.currentTimeMillis();
    }

    public ResultVO(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 成功响应
     */
    public static <T> ResultVO<T> success() {
        return new ResultVO<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    public static <T> ResultVO<T> success(T data) {
        return new ResultVO<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> ResultVO<T> success(String message, T data) {
        return new ResultVO<>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败响应
     */
    public static <T> ResultVO<T> fail() {
        return new ResultVO<>(ResultCode.FAILURE.getCode(), ResultCode.FAILURE.getMessage(), null);
    }

    public static <T> ResultVO<T> fail(String message) {
        return new ResultVO<>(ResultCode.FAILURE.getCode(), message, null);
    }

    public static <T> ResultVO<T> fail(int code, String message) {
        return new ResultVO<>(code, message, null);
    }

    public static <T> ResultVO<T> fail(ResultCode resultCode) {
        return new ResultVO<>(resultCode.getCode(), resultCode.getMessage(), null);
    }

    /**
     * 自定义状态码响应
     */
    public static <T> ResultVO<T> of(int code, String message, T data) {
        return new ResultVO<>(code, message, data);
    }
}
