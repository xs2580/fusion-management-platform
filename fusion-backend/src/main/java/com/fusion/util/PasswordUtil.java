package com.fusion.util;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * 密码工具类
 * <p>
 * 脚手架演示使用 MD5（与 sql/fusion_platform.sql 中初始化的密码保持一致）。
 * <b>生产环境请替换为 BCrypt / Argon2 等带盐慢哈希算法。</b>
 *
 * @author Fusion
 */
public final class PasswordUtil {

    /** 全局盐值，生产环境请放入配置中心 */
    private static final String DEFAULT_SALT = "fusion";

    private PasswordUtil() {
    }

    /**
     * 不加盐 MD5（用于兼容初始化 SQL）
     */
    public static String md5(String raw) {
        return DigestUtils.md5DigestAsHex(raw.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 加盐 MD5，推荐业务侧使用
     */
    public static String encrypt(String raw) {
        return md5(raw + DEFAULT_SALT);
    }

    /**
     * 校验明文与密文是否匹配（同时兼容加盐与不加盐两种存量密码）
     */
    public static boolean matches(String raw, String encoded) {
        if (raw == null || encoded == null) {
            return false;
        }
        return encoded.equalsIgnoreCase(md5(raw)) || encoded.equalsIgnoreCase(encrypt(raw));
    }
}
