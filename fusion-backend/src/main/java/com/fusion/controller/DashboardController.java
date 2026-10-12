package com.fusion.controller;

import com.fusion.common.Result;
import com.fusion.service.SysAdminService;
import com.fusion.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 后台首页看板数据
 *
 * @author Fusion
 */
@Tag(name = "04-数据看板", description = "后台首页统计")
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final SysUserService sysUserService;
    private final SysAdminService sysAdminService;

    @Operation(summary = "概览统计")
    @GetMapping("/overview")
    public Result<Map<String, Object>> overview() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("userTotal", sysUserService.count());
        data.put("userEnabled", sysUserService.lambdaQuery().eq(com.fusion.entity.SysUser::getStatus, 1).count());
        data.put("adminTotal", sysAdminService.count());
        data.put("adminEnabled", sysAdminService.lambdaQuery().eq(com.fusion.entity.SysAdmin::getStatus, 1).count());
        return Result.success(data);
    }
}
