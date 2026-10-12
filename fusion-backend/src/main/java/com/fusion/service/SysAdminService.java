package com.fusion.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fusion.common.PageResult;
import com.fusion.entity.SysAdmin;

/**
 * 管理员业务接口（Service 层）
 *
 * @author Fusion
 */
public interface SysAdminService extends IService<SysAdmin> {

    /**
     * 分页查询管理员
     */
    PageResult<SysAdmin> pageAdmins(Long current, Long size, String keyword, Integer status);

    /**
     * 新增管理员
     */
    boolean createAdmin(SysAdmin admin);

    /**
     * 修改管理员
     */
    boolean updateAdmin(SysAdmin admin);

    /**
     * 重置密码
     */
    boolean resetPassword(Long id, String newPassword);

    /**
     * 删除管理员（内置超级管理员不可删除）
     */
    boolean removeAdmin(Long id);
}
