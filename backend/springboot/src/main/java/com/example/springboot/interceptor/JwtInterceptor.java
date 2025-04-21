package com.example.springboot.interceptor;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.springboot.exception.ServiceException; // Your custom exception
import com.example.springboot.entity.User;
import com.example.springboot.mapper.UserMapper; // Or UserService if preferred
import com.example.springboot.utils.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(JwtInterceptor.class);

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private UserMapper userMapper; // Inject UserMapper or UserService to check user existence

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");

        // If not mapping to a method, pass directly (e.g., static resources)
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 1. Check for Authorization header and Bearer prefix
        if (StrUtil.isBlank(token) || !token.startsWith("Bearer ")) {
            log.warn("Authorization header missing or invalid format for path: {}", request.getRequestURI());
            // You might want to throw an exception or write to response directly
            // For simplicity with your Result structure, throwing an exception handled by GlobalExceptionHandler is common
            throw new ServiceException("401", "无token或格式错误，请登录"); // Use your constant/code
        }

        // 2. Extract token
        token = token.substring(7); // Remove "Bearer " prefix

        DecodedJWT decodedJWT;
        try {
            // 3. Validate token (signature, expiration)
            decodedJWT = tokenUtils.validateToken(token);
        } catch (JWTVerificationException e) {
            log.warn("JWT verification failed: {} for token: {}", e.getMessage(), token);
            throw new ServiceException("401", "Token验证失败或已过期，请重新登录");
        } catch (Exception e) {
            log.error("Error validating token", e);
            throw new ServiceException("500", "Token验证时发生错误");
        }

        // 4. Get user info from token and verify user exists in DB
        String username = tokenUtils.getUsernameFromToken(decodedJWT);
        Integer userId = tokenUtils.getUserIdFromToken(decodedJWT); // Make sure UserMapper has selectById
        User user = userMapper.selectByUserId(userId); // Fetch user from DB using ID from token

        if (user == null) {
            log.warn("User not found in DB for ID {} from token", userId);
            throw new ServiceException("401", "用户不存在，请重新登录");
        }

        // Optional: Check if user account is active/not disabled etc.
        // if (!user.isActive()) { ... }

        // 5. Store user info in request attribute for Controller access (Optional but convenient)
        // Note: Storing the full User object (without password) is common.
        user.setPassword(null); // Ensure password is not carried forward
        request.setAttribute("currentUser", user);
        request.setAttribute("currentUserId", userId); // Can also just store the ID

        log.info("JWT validation successful for user: {}, path: {}", username, request.getRequestURI());
        return true; // Token is valid, proceed with the request
    }
}

