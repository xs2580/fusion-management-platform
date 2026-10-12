package com.fusion.controller;

import com.fusion.common.PageResult;
import com.fusion.common.Result;
import com.fusion.entity.SysAdmin;
import com.fusion.service.SysAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员管理接口（后台「角色管理 - 管理员管理」）
 *
 * @author Fusion
 */
@Tag(name = "03-管理员管理", description = "sys_admin 的增删改查")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class SysAdminController {

    private final SysAdminService sysAdminService;

    @Operation(summary = "分页查询管理员列表")
    @GetMapping("/page")
    public Result<PageResult<SysAdmin>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        return Result.success(sysAdminService.pageAdmins(current, size, keyword, status));
    }

    @Operation(summary = "查询管理员详情")
    @GetMapping("/{id}")
    public Result<SysAdmin> detail(@PathVariable Long id) {
        return Result.success(sysAdminService.getById(id));
    }

    @Operation(summary = "新增管理员")
    @PostMapping
    public Result<Void> create(@RequestBody SysAdmin admin) {
        sysAdminService.createAdmin(admin);
        return Result.success("新增成功", null);
    }

    @Operation(summary = "修改管理员")
    @PutMapping
    public Result<Void> update(@RequestBody SysAdmin admin) {
        sysAdminService.updateAdmin(admin);
        return Result.success("修改成功", null);
    }

    @Operation(summary = "重置密码", description = "不传 newPassword 则重置为默认密码 123456")
    @PutMapping("/{id}/reset-password")
    public Result<Void> resetPassword(@PathVariable Long id,
                                      @RequestParam(required = false) String newPassword) {
        sysAdminService.resetPassword(id, newPassword);
        return Result.success("密码已重置", null);
    }

    @Operation(summary = "删除管理员（内置超管不可删）")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        sysAdminService.removeAdmin(id);
        return Result.success("删除成功", null);
    }
}
