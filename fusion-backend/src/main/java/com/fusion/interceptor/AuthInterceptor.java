package com.fusion.interceptor;

import com.fusion.common.LoginUser;
import com.fusion.common.UserContext;
import com.fusion.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录鉴权拦截器
 * <p>
 * 从请求头取 JWT → 解析 → 写入 {@link UserContext}。
 * 解析失败时由 {@link com.fusion.common.GlobalExceptionHandler} 统一转成 401 结构。
 *
 * @author Fusion
 */
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Value("${fusion.jwt.header:Authorization}")
    private String header;

    @Value("${fusion.jwt.prefix:Bearer }")
    private String prefix;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 放行预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader(header);
        if (StringUtils.hasText(token) && StringUtils.hasText(prefix) && token.startsWith(prefix.trim())) {
            token = token.substring(prefix.trim().length()).trim();
        }
        if (!StringUtils.hasText(token)) {
            token = request.getParameter("token");
        }
        if (!StringUtils.hasText(token)) {
            throw new com.fusion.common.BusinessException(com.fusion.common.ResultCode.UNAUTHORIZED);
        }

        Claims claims = jwtUtil.parseToken(token);
        LoginUser loginUser = new LoginUser(
                jwtUtil.getId(claims),
                jwtUtil.getUsername(claims),
                jwtUtil.getNickname(claims),
                jwtUtil.getType(claims));
        UserContext.set(loginUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        // 必须清理，避免线程复用导致的身份串号
        UserContext.clear();
    }
}
