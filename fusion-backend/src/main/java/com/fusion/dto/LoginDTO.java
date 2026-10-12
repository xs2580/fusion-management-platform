package com.fusion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * 登录请求参数
 *
 * @author Fusion
 */
@Data
@Schema(description = "登录请求参数")
public class LoginDTO implements Serializable {

    @NotBlank(message = "请输入登录账号")
    @Size(min = 2, max = 50, message = "账号长度需在 2~50 之间")
    @Schema(description = "登录账号", example = "admin")
    private String username;

    @NotBlank(message = "请输入登录密码")
    @Size(min = 5, max = 32, message = "密码长度需在 5~32 之间")
    @Schema(description = "登录密码", example = "admin")
    private String password;

    /**
     * 登录身份：user=普通用户  admin=管理员
     */
    @NotBlank(message = "请选择登录身份")
    @Schema(description = "登录身份：user 普通用户 / admin 管理员", example = "admin")
    private String loginType;
}
