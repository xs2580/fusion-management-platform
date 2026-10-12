-- =============================================================
--  融合管理系统平台 (Fusion Management Platform)
--  数据库脚本  ·  MySQL 8.0+
--  说明：可直接整段导入 Navicat / DataGrip / 命令行 source
--
--  默认账号：
--    管理员  admin / admin
--    普通用户 user  / 123456
--
--  密码算法：MD5(password)  32位小写
--    MD5('admin')  = 21232f297a57a5a743894a0e4a801fc3
--    MD5('123456') = e10adc3949ba59abbe56e057f20f883e
-- =============================================================

DROP DATABASE IF EXISTS `fusion_platform`;
CREATE DATABASE `fusion_platform`
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

USE `fusion_platform`;

-- -------------------------------------------------------------
-- 1. 管理员表 sys_admin
-- -------------------------------------------------------------
DROP TABLE IF EXISTS `sys_admin`;
CREATE TABLE `sys_admin`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username`    VARCHAR(50)  NOT NULL COMMENT '登录账号',
    `password`    VARCHAR(64)  NOT NULL COMMENT '登录密码(MD5)',
    `nickname`    VARCHAR(50)           DEFAULT NULL COMMENT '昵称',
    `avatar`      VARCHAR(255)          DEFAULT NULL COMMENT '头像地址',
    `email`       VARCHAR(100)          DEFAULT NULL COMMENT '邮箱',
    `phone`       VARCHAR(20)           DEFAULT NULL COMMENT '手机号',
    `role`        VARCHAR(20)  NOT NULL DEFAULT 'ADMIN' COMMENT '角色标识: SUPER_ADMIN/ADMIN',
    `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态: 1启用 0禁用',
    `last_login`  DATETIME              DEFAULT NULL COMMENT '最后登录时间',
    `remark`      VARCHAR(255)          DEFAULT NULL COMMENT '备注',
    `deleted`     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0正常 1已删除',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_admin_username` (`username`),
    KEY `idx_admin_status` (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统管理员表';

-- -------------------------------------------------------------
-- 2. 普通用户表 sys_user
-- -------------------------------------------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username`    VARCHAR(50)  NOT NULL COMMENT '登录账号',
    `password`    VARCHAR(64)  NOT NULL COMMENT '登录密码(MD5)',
    `nickname`    VARCHAR(50)           DEFAULT NULL COMMENT '昵称',
    `avatar`      VARCHAR(255)          DEFAULT NULL COMMENT '头像地址',
    `email`       VARCHAR(100)          DEFAULT NULL COMMENT '邮箱',
    `phone`       VARCHAR(20)           DEFAULT NULL COMMENT '手机号',
    `gender`      TINYINT      NOT NULL DEFAULT 0 COMMENT '性别: 0未知 1男 2女',
    `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态: 1启用 0禁用',
    `last_login`  DATETIME              DEFAULT NULL COMMENT '最后登录时间',
    `remark`      VARCHAR(255)          DEFAULT NULL COMMENT '备注',
    `deleted`     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0正常 1已删除',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_username` (`username`),
    KEY `idx_user_status` (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统普通用户表';

-- -------------------------------------------------------------
-- 3. 初始化数据
-- -------------------------------------------------------------
INSERT INTO `sys_admin` (`username`, `password`, `nickname`, `avatar`, `email`, `phone`, `role`, `status`, `remark`)
VALUES ('admin', '21232f297a57a5a743894a0e4a801fc3', '超级管理员', 'https://api.dicebear.com/7.x/bottts/svg?seed=admin',
        'admin@fusion.com', '13800000000', 'SUPER_ADMIN', 1, '系统内置超级管理员，请勿删除'),
       ('manager', '21232f297a57a5a743894a0e4a801fc3', '运营管理员', 'https://api.dicebear.com/7.x/bottts/svg?seed=manager',
        'manager@fusion.com', '13800000001', 'ADMIN', 1, '普通管理员账号');

INSERT INTO `sys_user` (`username`, `password`, `nickname`, `avatar`, `email`, `phone`, `gender`, `status`, `remark`)
VALUES ('user', 'e10adc3949ba59abbe56e057f20f883e', 'H1',
        '/avatar-default.webp', 'user@mail.com', '13988888888', 1, 1, '默认演示用户'),
       ('lisa', 'e10adc3949ba59abbe56e057f20f883e', 'Lisa',
        'https://api.dicebear.com/7.x/adventurer/svg?seed=lisa', 'lisa@mail.com', '13900001111', 2, 1, '普通用户'),
       ('tom', 'e10adc3949ba59abbe56e057f20f883e', 'Tom',
        'https://api.dicebear.com/7.x/adventurer/svg?seed=tom', 'tom@mail.com', '13900002222', 1, 1, '普通用户');

-- -------------------------------------------------------------
-- 4. 校验
-- -------------------------------------------------------------
SELECT '管理员' AS `表`, COUNT(*) AS `条数` FROM `sys_admin`
UNION ALL
SELECT '普通用户', COUNT(*) FROM `sys_user`;
