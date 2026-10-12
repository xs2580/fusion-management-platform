package com.fusion.util;

import com.fusion.common.BusinessException;
import com.fusion.common.ResultCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 *
 * @author Fusion
 */
@Slf4j
@Component
public class JwtUtil {

    private static final String CLAIM_ID = "id";
    private static final String CLAIM_USERNAME = "username";
    private static final String CLAIM_NICKNAME = "nickname";
    private static final String CLAIM_TYPE = "type";

    @Value("${fusion.jwt.secret}")
    private String secret;

    @Value("${fusion.jwt.expire:7200}")
    private Long expire;

    private SecretKey key() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成 token
     *
     * @param id       用户/管理员主键
     * @param username 账号
     * @param nickname 昵称
     * @param type     登录类型：user / admin
     */
    public String createToken(Long id, String username, String nickname, String type) {
        Date now = new Date();
        Date expireAt = new Date(now.getTime() + expire * 1000);

        Map<String, Object> claims = new HashMap<>(4);
        claims.put(CLAIM_ID, id);
        claims.put(CLAIM_USERNAME, username);
        claims.put(CLAIM_NICKNAME, nickname);
        claims.put(CLAIM_TYPE, type);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(id))
                .setIssuedAt(now)
                .setExpiration(expireAt)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析 token，失败抛出 {@link BusinessException}
     */
    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new BusinessException(ResultCode.TOKEN_EXPIRED);
        } catch (JwtException | IllegalArgumentException e) {
            throw new BusinessException(ResultCode.TOKEN_INVALID);
        }
    }

    public Long getId(Claims claims) {
        Object v = claims.get(CLAIM_ID);
        return v == null ? null : Long.valueOf(String.valueOf(v));
    }

    public String getUsername(Claims claims) {
        return String.valueOf(claims.get(CLAIM_USERNAME));
    }

    public String getNickname(Claims claims) {
        return String.valueOf(claims.get(CLAIM_NICKNAME));
    }

    public String getType(Claims claims) {
        return String.valueOf(claims.get(CLAIM_TYPE));
    }

    /** token 有效期（秒） */
    public Long getExpire() {
        return expire;
    }
}
