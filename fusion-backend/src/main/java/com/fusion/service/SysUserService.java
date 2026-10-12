package com.fusion.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fusion.common.PageResult;
import com.fusion.entity.SysUser;

import java.util.Map;

/**
 * 普通用户业务接口（Service 层）
 *
 * @author Fusion
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 分页查询用户
     *
     * @param current 当前页
     * @param size    每页条数
     * @param keyword 账号 / 昵称 / 手机号 模糊关键字
     * @param status  状态，null 表示全部
     */
    PageResult<SysUser> pageUsers(Long current, Long size, String keyword, Integer status);

    /**
     * 新增用户
     */
    boolean createUser(SysUser user);

    /**
     * 修改用户（不允许改密码，密码走独立接口）
     */
    boolean updateUser(SysUser user);

    /**
     * 重置密码
     */
    boolean resetPassword(Long id, String newPassword);

    /**
     * 删除用户（逻辑删除）
     */
    boolean removeUser(Long id);

    /**
     * 统计数据（用于后台首页看板）
     */
    Map<String, Object> statistics();
}
