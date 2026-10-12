package com.fusion.service.impl;

import com.fusion.common.BusinessException;
import com.fusion.common.LoginUser;
import com.fusion.common.ResultCode;
import com.fusion.common.UserContext;
import com.fusion.dto.LoginDTO;
import com.fusion.dto.PasswordDTO;
import com.fusion.dto.RegisterDTO;
import com.fusion.entity.SysAdmin;
import com.fusion.entity.SysUser;
import com.fusion.service.AuthService;
import com.fusion.service.SysAdminService;
import com.fusion.service.SysUserService;
import com.fusion.util.JwtUtil;
import com.fusion.util.PasswordUtil;
import com.fusion.vo.LoginVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * 认证业务实现
 *
 * @author Fusion
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class AuthServiceImpl implements AuthService {

    /** 登录类型常量 */
    private static final String TYPE_USER = "user";
    private static final String TYPE_ADMIN = "admin";

    /** 默认头像 */
    private static final String DEFAULT_AVATAR = "/avatar-default.webp";

    private final SysUserService sysUserService;
    private final SysAdminService sysAdminService;
    private final JwtUtil jwtUtil;

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        String type = StringUtils.hasText(loginDTO.getLoginType())
                ? loginDTO.getLoginType().toLowerCase()
                : TYPE_USER;

        return TYPE_ADMIN.equals(type) ? adminLogin(loginDTO) : userLogin(loginDTO);
    }

    /** 管理员登录：只查 sys_admin */
    private LoginVO adminLogin(LoginDTO dto) {
        SysAdmin admin = sysAdminService.lambdaQuery()
                .eq(SysAdmin::getUsername, dto.getUsername())
                .one();
        // 统一提示，避免暴露账号是否存在
        BusinessException.throwIf(admin == null, ResultCode.LOGIN_FAILED);
        BusinessException.throwIf(!PasswordUtil.matches(dto.getPassword(), admin.getPassword()),
                ResultCode.LOGIN_FAILED);
        BusinessException.throwIf(admin.getStatus() != null && admin.getStatus() == 0,
                ResultCode.ACCOUNT_DISABLED);

        // 更新最后登录时间
        SysAdmin touch = new SysAdmin();
        touch.setId(admin.getId());
        touch.setLastLogin(new Date());
        sysAdminService.updateById(touch);

        LoginVO vo = new LoginVO();
        vo.setToken(jwtUtil.createToken(admin.getId(), admin.getUsername(), admin.getNickname(), TYPE_ADMIN));
        vo.setExpire(jwtUtil.getExpire());
        vo.setId(admin.getId());
        vo.setUsername(admin.getUsername());
        vo.setNickname(admin.getNickname());
        vo.setAvatar(admin.getAvatar());
        vo.setEmail(admin.getEmail());
        vo.setPhone(admin.getPhone());
        vo.setRole(admin.getRole());
        vo.setLoginType(TYPE_ADMIN);
        vo.setLastLogin(admin.getLastLogin());
        return vo;
    }

    /** 普通用户登录：只查 sys_user */
    private LoginVO userLogin(LoginDTO dto) {
        SysUser user = sysUserService.lambdaQuery()
                .eq(SysUser::getUsername, dto.getUsername())
                .one();
        BusinessException.throwIf(user == null, ResultCode.LOGIN_FAILED);
        BusinessException.throwIf(!PasswordUtil.matches(dto.getPassword(), user.getPassword()),
                ResultCode.LOGIN_FAILED);
        BusinessException.throwIf(user.getStatus() != null && user.getStatus() == 0,
                ResultCode.ACCOUNT_DISABLED);

        SysUser touch = new SysUser();
        touch.setId(user.getId());
        touch.setLastLogin(new Date());
        sysUserService.updateById(touch);

        LoginVO vo = new LoginVO();
        vo.setToken(jwtUtil.createToken(user.getId(), user.getUsername(), user.getNickname(), TYPE_USER));
        vo.setExpire(jwtUtil.getExpire());
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        vo.setEmail(user.getEmail());
        vo.setPhone(user.getPhone());
        vo.setGender(user.getGender());
        vo.setRole(TYPE_USER);
        vo.setLoginType(TYPE_USER);
        vo.setLastLogin(user.getLastLogin());
        return vo;
    }

    @Override
    public LoginVO register(RegisterDTO dto) {
        BusinessException.throwIf(!dto.getPassword().equals(dto.getConfirmPassword()), "两次输入的密码不一致");

        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setNickname(StringUtils.hasText(dto.getNickname()) ? dto.getNickname() : dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setAvatar(DEFAULT_AVATAR);
        user.setGender(0);
        user.setStatus(1);
        sysUserService.createUser(user);

        // 注册成功直接登录，免二次输入
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(dto.getUsername());
        loginDTO.setPassword(dto.getPassword());
        loginDTO.setLoginType(TYPE_USER);
        return userLogin(loginDTO);
    }

    @Override
    public LoginVO currentUser() {
        LoginUser current = UserContext.require();
        if (current.isAdmin()) {
            SysAdmin admin = sysAdminService.getById(current.getId());
            BusinessException.throwIf(admin == null, ResultCode.ACCOUNT_NOT_EXISTS);
            LoginVO vo = new LoginVO();
            vo.setId(admin.getId());
            vo.setUsername(admin.getUsername());
            vo.setNickname(admin.getNickname());
            vo.setAvatar(admin.getAvatar());
            vo.setEmail(admin.getEmail());
            vo.setPhone(admin.getPhone());
            vo.setRole(admin.getRole());
            vo.setLoginType(TYPE_ADMIN);
            vo.setLastLogin(admin.getLastLogin());
            return vo;
        }
        SysUser user = sysUserService.getById(current.getId());
        BusinessException.throwIf(user == null, ResultCode.ACCOUNT_NOT_EXISTS);
        LoginVO vo = new LoginVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        vo.setEmail(user.getEmail());
        vo.setPhone(user.getPhone());
        vo.setGender(user.getGender());
        vo.setRole(TYPE_USER);
        vo.setLoginType(TYPE_USER);
        vo.setLastLogin(user.getLastLogin());
        return vo;
    }

    @Override
    public boolean changePassword(PasswordDTO dto) {
        BusinessException.throwIf(!dto.getNewPassword().equals(dto.getConfirmPassword()), "两次输入的新密码不一致");
        LoginUser current = UserContext.require();

        if (current.isAdmin()) {
            SysAdmin admin = sysAdminService.getById(current.getId());
            BusinessException.throwIf(admin == null, ResultCode.ACCOUNT_NOT_EXISTS);
            BusinessException.throwIf(!PasswordUtil.matches(dto.getOldPassword(), admin.getPassword()),
                    ResultCode.OLD_PASSWORD_ERROR);
            SysAdmin update = new SysAdmin();
            update.setId(admin.getId());
            update.setPassword(PasswordUtil.encrypt(dto.getNewPassword()));
            return sysAdminService.updateById(update);
        }

        SysUser user = sysUserService.getById(current.getId());
        BusinessException.throwIf(user == null, ResultCode.ACCOUNT_NOT_EXISTS);
        BusinessException.throwIf(!PasswordUtil.matches(dto.getOldPassword(), user.getPassword()),
                ResultCode.OLD_PASSWORD_ERROR);
        SysUser update = new SysUser();
        update.setId(user.getId());
        update.setPassword(PasswordUtil.encrypt(dto.getNewPassword()));
        return sysUserService.updateById(update);
    }

    @Override
    public LoginVO updateProfile(LoginVO profile) {
        LoginUser current = UserContext.require();

        if (current.isAdmin()) {
            SysAdmin update = new SysAdmin();
            update.setId(current.getId());
            update.setNickname(profile.getNickname());
            update.setEmail(profile.getEmail());
            update.setPhone(profile.getPhone());
            update.setAvatar(profile.getAvatar());
            sysAdminService.updateById(update);
        } else {
            SysUser update = new SysUser();
            update.setId(current.getId());
            update.setNickname(profile.getNickname());
            update.setEmail(profile.getEmail());
            update.setPhone(profile.getPhone());
            update.setAvatar(profile.getAvatar());
            update.setGender(profile.getGender());
            sysUserService.updateById(update);
        }
        return currentUser();
    }
}
