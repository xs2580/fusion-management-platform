package com.fusion.controller;

import com.fusion.common.Result;
import com.fusion.dto.LoginDTO;
import com.fusion.dto.PasswordDTO;
import com.fusion.dto.RegisterDTO;
import com.fusion.service.AuthService;
import com.fusion.vo.LoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证接口（登录 / 注册 / 当前用户 / 修改密码）
 * <p>
 * Controller 层：只做参数接收与结果封装，不写业务逻辑。
 *
 * @author Fusion
 */
@Tag(name = "01-认证管理", description = "登录、注册、个人资料")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "登录", description = "loginType = user 普通用户 / admin 管理员")
    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody @Valid LoginDTO loginDTO) {
        return Result.success("登录成功", authService.login(loginDTO));
    }

    @Operation(summary = "注册", description = "前台普通用户自助注册，注册成功即自动登录")
    @PostMapping("/register")
    public Result<LoginVO> register(@RequestBody @Valid RegisterDTO registerDTO) {
        return Result.success("注册成功", authService.register(registerDTO));
    }

    @Operation(summary = "获取当前登录人信息")
    @GetMapping("/info")
    public Result<LoginVO> info() {
        return Result.success(authService.currentUser());
    }

    @Operation(summary = "更新当前登录人资料")
    @PutMapping("/profile")
    public Result<LoginVO> updateProfile(@RequestBody LoginVO profile) {
        return Result.success("资料已更新", authService.updateProfile(profile));
    }

    @Operation(summary = "修改密码")
    @PutMapping("/password")
    public Result<Void> changePassword(@RequestBody @Valid PasswordDTO passwordDTO) {
        authService.changePassword(passwordDTO);
        return Result.success("密码修改成功，请重新登录", null);
    }

    @Operation(summary = "退出登录", description = "JWT 无状态，前端清除本地 token 即可，此接口用于埋点/审计")
    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.success("已退出登录", null);
    }
}
