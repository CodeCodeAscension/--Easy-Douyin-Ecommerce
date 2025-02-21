package com.example.auth.util;

import com.example.common.exception.SystemException;
import com.example.auth.config.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 *     Redis工具类，用来存储用户登陆的Token
 * </p>
 * @author vlsmb
 */
@Component
public class TokenRedisUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private JwtConfig jwtConfig;

    /**
     * 用户ID转为redis access-token键名
     * @param userId 用户ID
     * @return redisKey名
     */
    private String userIdToAccessKey(Long userId) {
        return "access-token:" + userId;
    }

    /**
     * 用户ID转为redis refresh-token键名
     * @param userId 用户ID
     * @return redisKey名
     */
    private String userIdToRefreshKey(Long userId) {
        return "refresh-token:" + userId;
    }

    /**
     * 向redis中保存用户当前登陆AccessToken
     * @param userId 用户ID
     * @param accessToken 权限Token
     * @param refreshToken 刷新token
     */
    public void addToken(Long userId, String accessToken, String refreshToken) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(userIdToAccessKey(userId), accessToken, jwtConfig.getExpireHour(), TimeUnit.HOURS);
        operations.set(userIdToRefreshKey(userId), refreshToken, jwtConfig.getRefreshExpireDay(), TimeUnit.DAYS);
    }

    /**
     * 删除某用户的AccessToken和RefreshToken
     * @param userId 用户ID
     */
    public void removeToken(Long userId) {
        redisTemplate.delete(userIdToAccessKey(userId));
        redisTemplate.delete(userIdToRefreshKey(userId));
    }

    /**
     * 获得某用户AccessToken
     * @param userId 用户ID
     * @return accessToken
     */
    public String getAccessToken(Long userId) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        return operations.get(userIdToAccessKey(userId));
    }

    /**
     * 获得某用户RefreshToken
     * @param userId 用户ID
     * @return refreshToken
     */
    public String getRefreshToken(Long userId) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        return operations.get(userIdToRefreshKey(userId));
    }
}
