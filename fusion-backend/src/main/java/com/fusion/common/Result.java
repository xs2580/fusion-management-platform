package com.fusion.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 统一返回结果封装
 * <p>
 * 前后端约定：所有接口一律返回该结构，前端只需判断 {@code code == 200}。
 *
 * <pre>
 * {
 *   "code": 200,
 *   "message": "操作成功",
 *   "data": { ... },
 *   "timestamp": 1730000000000
 * }
 * </pre>
 *
 * @param <T> 业务数据类型
 * @author Fusion
 */
@Data
@Accessors(chain = true)
@Schema(description = "统一返回结果")
public class Result<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 业务状态码，200 表示成功 */
    @Schema(description = "状态码，200 为成功")
    private Integer code;

    /** 提示信息 */
    @Schema(description = "提示信息")
    private String message;

    /** 业务数据 */
    @Schema(description = "业务数据")
    private T data;

    /** 服务端时间戳，便于前端排查 */
    @Schema(description = "服务端时间戳(毫秒)")
    private Long timestamp;

    /* ==================== 成功 ==================== */

    public static <T> Result<T> success() {
        return build(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    public static <T> Result<T> success(T data) {
        return build(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> Result<T> success(String message, T data) {
        return build(ResultCode.SUCCESS.getCode(), message, data);
    }

    public static <T> Result<T> success(ResultCode resultCode, T data) {
        return build(resultCode.getCode(), resultCode.getMessage(), data);
    }

    /* ==================== 失败 ==================== */

    public static <T> Result<T> error() {
        return build(ResultCode.ERROR.getCode(), ResultCode.ERROR.getMessage(), null);
    }

    public static <T> Result<T> error(String message) {
        return build(ResultCode.ERROR.getCode(), message, null);
    }

    public static <T> Result<T> error(Integer code, String message) {
        return build(code, message, null);
    }

    public static <T> Result<T> error(ResultCode resultCode) {
        return build(resultCode.getCode(), resultCode.getMessage(), null);
    }

    public static <T> Result<T> error(ResultCode resultCode, String message) {
        return build(resultCode.getCode(), message, null);
    }

    /* ==================== 内部构造 ==================== */

    private static <T> Result<T> build(Integer code, String message, T data) {
        return new Result<T>()
                .setCode(code)
                .setMessage(message)
                .setData(data)
                .setTimestamp(System.currentTimeMillis());
    }

    /** 快捷判断是否成功 */
    public boolean isSuccess() {
        return ResultCode.SUCCESS.getCode().equals(this.code);
    }
}
