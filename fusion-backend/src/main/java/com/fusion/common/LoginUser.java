package com.fusion.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 当前登录人上下文（由拦截器解析 JWT 后放入 ThreadLocal）
 *
 * @author Fusion
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 登录账号 */
    private String username;

    /** 昵称 */
    private String nickname;

    /** 登录类型：user=普通用户  admin=管理员 */
    private String type;

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(type);
    }

    public boolean isUser() {
        return "user".equalsIgnoreCase(type);
    }
}
