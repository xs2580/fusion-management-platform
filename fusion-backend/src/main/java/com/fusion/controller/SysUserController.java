package com.fusion.controller;

import com.fusion.common.PageResult;
import com.fusion.common.Result;
import com.fusion.entity.SysUser;
import com.fusion.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户管理接口（后台「角色管理 - 用户管理」）
 *
 * @author Fusion
 */
@Tag(name = "02-用户管理", description = "sys_user 的增删改查")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService sysUserService;

    @Operation(summary = "分页查询用户列表")
    @GetMapping("/page")
    public Result<PageResult<SysUser>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        return Result.success(sysUserService.pageUsers(current, size, keyword, status));
    }

    @Operation(summary = "查询用户详情")
    @GetMapping("/{id}")
    public Result<SysUser> detail(@PathVariable Long id) {
        return Result.success(sysUserService.getById(id));
    }

    @Operation(summary = "新增用户")
    @PostMapping
    public Result<Void> create(@RequestBody SysUser user) {
        sysUserService.createUser(user);
        return Result.success("新增成功", null);
    }

    @Operation(summary = "修改用户")
    @PutMapping
    public Result<Void> update(@RequestBody SysUser user) {
        sysUserService.updateUser(user);
        return Result.success("修改成功", null);
    }

    @Operation(summary = "重置密码", description = "不传 newPassword 则重置为默认密码 123456")
    @PutMapping("/{id}/reset-password")
    public Result<Void> resetPassword(@PathVariable Long id,
                                      @RequestParam(required = false) String newPassword) {
        sysUserService.resetPassword(id, newPassword);
        return Result.success("密码已重置", null);
    }

    @Operation(summary = "删除用户（逻辑删除）")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        sysUserService.removeUser(id);
        return Result.success("删除成功", null);
    }

    @Operation(summary = "用户统计")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> statistics() {
        return Result.success(sysUserService.statistics());
    }
}
