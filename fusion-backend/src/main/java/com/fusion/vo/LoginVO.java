package com.fusion.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 登录成功返回的用户信息
 *
 * @author Fusion
 */
@Data
@Schema(description = "登录返回信息")
public class LoginVO implements Serializable {

    @Schema(description = "登录令牌")
    private String token;

    @Schema(description = "令牌有效期（秒）")
    private Long expire;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "登录账号")
    private String username;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "性别：0未知 1男 2女")
    private Integer gender;

    @Schema(description = "角色标识")
    private String role;

    @Schema(description = "登录类型：user / admin")
    private String loginType;

    @Schema(description = "最后登录时间")
    private Date lastLogin;
}
