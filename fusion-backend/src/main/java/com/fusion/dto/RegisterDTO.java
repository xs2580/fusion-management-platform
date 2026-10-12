package com.fusion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * 注册请求参数（前台用户自助注册）
 *
 * @author Fusion
 */
@Data
@Schema(description = "注册请求参数")
public class RegisterDTO implements Serializable {

    @NotBlank(message = "请输入登录账号")
    @Size(min = 3, max = 20, message = "账号长度需在 3~20 之间")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]*$", message = "账号需以字母开头，仅含字母、数字、下划线")
    @Schema(description = "登录账号")
    private String username;

    @NotBlank(message = "请输入登录密码")
    @Size(min = 6, max = 20, message = "密码长度需在 6~20 之间")
    @Schema(description = "登录密码")
    private String password;

    @NotBlank(message = "请再次输入密码")
    @Schema(description = "确认密码")
    private String confirmPassword;

    @Size(max = 20, message = "昵称最长 20 个字符")
    @Schema(description = "昵称")
    private String nickname;

    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱")
    private String email;

    @Pattern(regexp = "^$|^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "手机号")
    private String phone;
}
