package com.fusion.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 普通用户实体（对应表 sys_user）
 *
 * @author Fusion
 */
@Data
@TableName("sys_user")
@Schema(description = "普通用户")
public class SysUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "登录账号")
    private String username;

    /** 只写不读：接口返回时不会带出密码 */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "登录密码(仅接收，不返回)")
    private String password;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像地址")
    private String avatar;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "性别：0未知 1男 2女")
    private Integer gender;

    @Schema(description = "状态：1启用 0禁用")
    private Integer status;

    @Schema(description = "最后登录时间")
    private Date lastLogin;

    @Schema(description = "备注")
    private String remark;

    @TableLogic
    @TableField(select = false)
    @Schema(description = "逻辑删除标记")
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private Date updateTime;
}
