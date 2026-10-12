package com.fusion.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fusion.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 普通用户 Mapper
 *
 * @author Fusion
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
}
