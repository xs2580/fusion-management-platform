package com.fusion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * 修改密码请求参数
 *
 * @author Fusion
 */
@Data
@Schema(description = "修改密码请求参数")
public class PasswordDTO implements Serializable {

    @NotBlank(message = "请输入原密码")
    @Schema(description = "原密码")
    private String oldPassword;

    @NotBlank(message = "请输入新密码")
    @Size(min = 6, max = 20, message = "新密码长度需在 6~20 之间")
    @Schema(description = "新密码")
    private String newPassword;

    @NotBlank(message = "请确认新密码")
    @Schema(description = "确认新密码")
    private String confirmPassword;
}
