package com.example.user.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.common.exception.UnauthorizedException;
import com.example.user.config.JwtConfig;
import com.example.user.domain.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *     jwt工具类
 * </p>
 * @author vlsmb
 */
@Component
public class JwtUtil {

    @Autowired
    private JwtConfig jwtConfig;

    /**
     * 计算令牌过期时间
     * @return Date对象
     */
    private Date expireTime() {
        return new Date(System.currentTimeMillis() + 1000L * 60 * 60 * jwtConfig.getExpireHour());
    }

    /**
     * 生成JWT令牌
     * @param user 用户对象
     * @return JWT令牌字符串
     */
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        if(user == null || user.getUserId() == null || user.getPower() == null) {
            throw new RuntimeException("给JWT传递的User对象缺少必要信息");
        }
        claims.put("userId", user.getUserId());
        claims.put("userPower", user.getPower().getCode());
        return JWT.create()
                .withClaim("claims", claims)
                .withExpiresAt(expireTime())
                .sign(Algorithm.HMAC256(jwtConfig.getKey()));
    }

    /**
     * 获得JWT令牌信息
     * @param token
     * @return
     */
    public Map<String, Object> verifyToken(String token) throws UnauthorizedException {
        try {
            return JWT.require(Algorithm.HMAC256(jwtConfig.getKey()))
                    .build()
                    .verify(token)
                    .getClaim("claims")
                    .asMap();
        } catch (Exception e) {
            throw new UnauthorizedException("jwt令牌无效或者已经过期");
        }
    }
}
