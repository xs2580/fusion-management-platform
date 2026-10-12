package com.fusion.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fusion.common.BusinessException;
import com.fusion.common.PageResult;
import com.fusion.common.ResultCode;
import com.fusion.entity.SysAdmin;
import com.fusion.mapper.SysAdminMapper;
import com.fusion.service.SysAdminService;
import com.fusion.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 管理员业务实现
 *
 * @author Fusion
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysAdminServiceImpl extends ServiceImpl<SysAdminMapper, SysAdmin> implements SysAdminService {

    private static final String DEFAULT_PASSWORD = "123456";
    private static final String SUPER_ADMIN = "SUPER_ADMIN";

    @Override
    public PageResult<SysAdmin> pageAdmins(Long current, Long size, String keyword, Integer status) {
        LambdaQueryWrapper<SysAdmin> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            String kw = keyword.trim();
            wrapper.and(w -> w.like(SysAdmin::getUsername, kw)
                    .or().like(SysAdmin::getNickname, kw)
                    .or().like(SysAdmin::getPhone, kw)
                    .or().like(SysAdmin::getEmail, kw));
        }
        wrapper.eq(status != null, SysAdmin::getStatus, status)
                .orderByDesc(SysAdmin::getId);

        Page<SysAdmin> page = this.page(new Page<>(current, size), wrapper);
        return PageResult.of(page);
    }

    @Override
    public boolean createAdmin(SysAdmin admin) {
        BusinessException.throwIf(!StringUtils.hasText(admin.getUsername()), "账号不能为空");
        BusinessException.throwIf(!StringUtils.hasText(admin.getPassword()), "密码不能为空");

        Long count = this.lambdaQuery().eq(SysAdmin::getUsername, admin.getUsername()).count();
        BusinessException.throwIf(count > 0, ResultCode.USERNAME_EXISTS);

        admin.setId(null);
        admin.setPassword(PasswordUtil.encrypt(admin.getPassword()));
        if (admin.getStatus() == null) {
            admin.setStatus(1);
        }
        if (!StringUtils.hasText(admin.getRole())) {
            admin.setRole("ADMIN");
        }
        if (!StringUtils.hasText(admin.getNickname())) {
            admin.setNickname(admin.getUsername());
        }
        if (!StringUtils.hasText(admin.getAvatar())) {
            admin.setAvatar("https://api.dicebear.com/7.x/bottts/svg?seed=" + admin.getUsername());
        }
        return this.save(admin);
    }

    @Override
    public boolean updateAdmin(SysAdmin admin) {
        BusinessException.throwIf(admin.getId() == null, "缺少管理员ID");
        SysAdmin exists = this.getById(admin.getId());
        BusinessException.throwIf(exists == null, ResultCode.DATA_NOT_EXISTS);

        // 内置超级管理员不允许被降级 / 禁用
        if (SUPER_ADMIN.equals(exists.getRole())) {
            BusinessException.throwIf(admin.getStatus() != null && admin.getStatus() == 0,
                    ResultCode.DELETE_ADMIN_FORBIDDEN, "内置超级管理员不允许被禁用");
            admin.setRole(SUPER_ADMIN);
        }

        if (StringUtils.hasText(admin.getUsername()) && !admin.getUsername().equals(exists.getUsername())) {
            Long count = this.lambdaQuery().eq(SysAdmin::getUsername, admin.getUsername()).count();
            BusinessException.throwIf(count > 0, ResultCode.USERNAME_EXISTS);
        }
        admin.setPassword(null);
        return this.updateById(admin);
    }

    @Override
    public boolean resetPassword(Long id, String newPassword) {
        SysAdmin exists = this.getById(id);
        BusinessException.throwIf(exists == null, ResultCode.DATA_NOT_EXISTS);
        String pwd = StringUtils.hasText(newPassword) ? newPassword : DEFAULT_PASSWORD;
        SysAdmin update = new SysAdmin();
        update.setId(id);
        update.setPassword(PasswordUtil.encrypt(pwd));
        return this.updateById(update);
    }

    @Override
    public boolean removeAdmin(Long id) {
        BusinessException.throwIf(id == null, "缺少管理员ID");
        SysAdmin exists = this.getById(id);
        BusinessException.throwIf(exists == null, ResultCode.DATA_NOT_EXISTS);
        BusinessException.throwIf(SUPER_ADMIN.equals(exists.getRole()), ResultCode.DELETE_ADMIN_FORBIDDEN);
        return this.removeById(id);
    }
}
