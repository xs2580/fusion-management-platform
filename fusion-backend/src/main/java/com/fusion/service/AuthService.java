package com.fusion.service;

import com.fusion.dto.LoginDTO;
import com.fusion.dto.PasswordDTO;
import com.fusion.dto.RegisterDTO;
import com.fusion.vo.LoginVO;

/**
 * 认证业务接口（Service 层）
 *
 * @author Fusion
 */
public interface AuthService {

    /**
     * 登录（根据 loginType 分别校验 sys_admin / sys_user）
     */
    LoginVO login(LoginDTO loginDTO);

    /**
     * 前台用户注册
     */
    LoginVO register(RegisterDTO registerDTO);

    /**
     * 查询当前登录人信息
     */
    LoginVO currentUser();

    /**
     * 修改当前登录人密码
     */
    boolean changePassword(PasswordDTO passwordDTO);

    /**
     * 更新当前登录人基础资料（昵称 / 邮箱 / 手机 / 头像 / 性别）
     */
    LoginVO updateProfile(LoginVO profile);
}
