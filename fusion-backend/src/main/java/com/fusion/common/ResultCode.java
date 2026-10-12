package com.fusion.common;

import lombok.Getter;

/**
 * 统一状态码枚举
 *
 * @author Fusion
 */
@Getter
public enum ResultCode {

    /* ---------- 通用 ---------- */
    SUCCESS(200, "操作成功"),
    ERROR(500, "系统繁忙，请稍后再试"),
    PARAM_ERROR(400, "请求参数不合法"),
    UNAUTHORIZED(401, "登录状态已失效，请重新登录"),
    FORBIDDEN(403, "没有访问权限"),
    NOT_FOUND(404, "请求的资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方式不正确"),

    /* ---------- 认证相关 1xxx ---------- */
    LOGIN_FAILED(1001, "账号或密码错误"),
    TOKEN_EXPIRED(1002, "登录已过期，请重新登录"),
    TOKEN_INVALID(1003, "登录凭证无效"),
    ACCOUNT_DISABLED(1004, "账号已被禁用，请联系管理员"),
    USERNAME_EXISTS(1005, "该账号已被注册"),
    OLD_PASSWORD_ERROR(1006, "原密码不正确"),
    ACCOUNT_NOT_EXISTS(1007, "账号不存在"),

    /* ---------- 业务相关 2xxx ---------- */
    DATA_NOT_EXISTS(2001, "数据不存在或已被删除"),
    DATA_EXISTS(2002, "数据已存在，请勿重复添加"),
    OPERATE_FAILED(2003, "操作失败"),
    DELETE_ADMIN_FORBIDDEN(2004, "内置超级管理员不允许删除或禁用");

    /** 状态码 */
    private final Integer code;

    /** 提示信息 */
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
