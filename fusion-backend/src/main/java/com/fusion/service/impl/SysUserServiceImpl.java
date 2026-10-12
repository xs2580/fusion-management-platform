package com.fusion.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fusion.common.BusinessException;
import com.fusion.common.PageResult;
import com.fusion.common.ResultCode;
import com.fusion.entity.SysUser;
import com.fusion.mapper.SysUserMapper;
import com.fusion.service.SysUserService;
import com.fusion.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 普通用户业务实现
 *
 * @author Fusion
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private static final String DEFAULT_PASSWORD = "123456";

    @Override
    public PageResult<SysUser> pageUsers(Long current, Long size, String keyword, Integer status) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            String kw = keyword.trim();
            wrapper.and(w -> w.like(SysUser::getUsername, kw)
                    .or().like(SysUser::getNickname, kw)
                    .or().like(SysUser::getPhone, kw)
                    .or().like(SysUser::getEmail, kw));
        }
        wrapper.eq(status != null, SysUser::getStatus, status)
                .orderByDesc(SysUser::getId);

        Page<SysUser> page = this.page(new Page<>(current, size), wrapper);
        return PageResult.of(page);
    }

    @Override
    public boolean createUser(SysUser user) {
        BusinessException.throwIf(!StringUtils.hasText(user.getUsername()), "账号不能为空");
        BusinessException.throwIf(!StringUtils.hasText(user.getPassword()), "密码不能为空");

        Long count = this.lambdaQuery().eq(SysUser::getUsername, user.getUsername()).count();
        BusinessException.throwIf(count > 0, ResultCode.USERNAME_EXISTS);

        user.setId(null);
        user.setPassword(PasswordUtil.encrypt(user.getPassword()));
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
        if (user.getGender() == null) {
            user.setGender(0);
        }
        if (!StringUtils.hasText(user.getNickname())) {
            user.setNickname(user.getUsername());
        }
        if (!StringUtils.hasText(user.getAvatar())) {
            user.setAvatar("/avatar-default.webp");
        }
        return this.save(user);
    }

    @Override
    public boolean updateUser(SysUser user) {
        BusinessException.throwIf(user.getId() == null, "缺少用户ID");
        SysUser exists = this.getById(user.getId());
        BusinessException.throwIf(exists == null, ResultCode.DATA_NOT_EXISTS);

        // 账号唯一性校验
        if (StringUtils.hasText(user.getUsername()) && !user.getUsername().equals(exists.getUsername())) {
            Long count = this.lambdaQuery().eq(SysUser::getUsername, user.getUsername()).count();
            BusinessException.throwIf(count > 0, ResultCode.USERNAME_EXISTS);
        }
        // 密码走独立接口，这里强制置空避免被覆盖
        user.setPassword(null);
        return this.updateById(user);
    }

    @Override
    public boolean resetPassword(Long id, String newPassword) {
        SysUser exists = this.getById(id);
        BusinessException.throwIf(exists == null, ResultCode.DATA_NOT_EXISTS);
        String pwd = StringUtils.hasText(newPassword) ? newPassword : DEFAULT_PASSWORD;
        SysUser update = new SysUser();
        update.setId(id);
        update.setPassword(PasswordUtil.encrypt(pwd));
        return this.updateById(update);
    }

    @Override
    public boolean removeUser(Long id) {
        BusinessException.throwIf(id == null, "缺少用户ID");
        SysUser exists = this.getById(id);
        BusinessException.throwIf(exists == null, ResultCode.DATA_NOT_EXISTS);
        return this.removeById(id);
    }

    @Override
    public Map<String, Object> statistics() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("total", this.count());
        map.put("enabled", this.lambdaQuery().eq(SysUser::getStatus, 1).count());
        map.put("disabled", this.lambdaQuery().eq(SysUser::getStatus, 0).count());
        return map;
    }
}
