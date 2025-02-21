package com.example.auth.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.auth.domain.UserClaims;
import com.example.common.exception.UnauthorizedException;
import com.example.auth.config.JwtConfig;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 *     jwt工具类
 * </p>
 * @author vlsmb
 */
@Component
@AllArgsConstructor
public class JwtUtil {

    private final JwtConfig jwtConfig;

    /**
     * 计算令牌过期时间
     * @return Date对象
     */
    private Date expireTime(int hours) {
        return new Date(System.currentTimeMillis() + 1000L * 60 * 60 * hours);
    }

    /**
     * 生成JWT令牌
     * @param userClaims 用户信息
     * @param hours 过期时间
     * @return JWT令牌字符串
     */
    private String generateToken(UserClaims userClaims, int hours) {
        return JWT.create()
                .withClaim("claims", userClaims.toClaims())
                .withExpiresAt(expireTime(hours))
                .sign(Algorithm.HMAC256(jwtConfig.getKey()));
    }

    /**
     * 生成用户AccessToken
     * @param userClaims 用户信息
     * @return AccessToken
     */
    public String generateAccessToken(UserClaims userClaims) {
        return generateToken(userClaims, jwtConfig.getExpireHour());
    }

    /**
     * 生成用户RefreshToken
     * @param userClaims 用户信息
     * @return RefreshToken
     */
    public String generateRefreshToken(UserClaims userClaims) {
        return generateToken(userClaims, jwtConfig.getRefreshExpireDay() * 24);
    }

    /**
     * 获得JWT令牌信息
     * @param token JWT令牌
     * @return UserClaims对象
     * @throws UnauthorizedException JWT令牌失效异常
     */
    public UserClaims verifyToken(String token) throws UnauthorizedException {
        try {
            Map<String, Object> claims= JWT.require(Algorithm.HMAC256(jwtConfig.getKey()))
                    .build()
                    .verify(token)
                    .getClaim("claims")
                    .asMap();
            return new UserClaims(claims);
        } catch (Exception e) {
            throw new UnauthorizedException("token无效或者已经过期");
        }
    }
}
