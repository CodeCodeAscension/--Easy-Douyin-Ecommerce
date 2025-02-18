package com.example.user.util;

import com.example.common.exception.SystemException;
import com.example.user.config.JwtConfig;
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
public class RedisUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private JwtConfig jwtConfig;

    /**
     * 用户ID转为redisKey名
     * @param userId 用户ID
     * @return redisKey名
     */
    private String userIdToKey(Long userId) {
        return "token:" + userId;
    }

    /**
     * 向redis中保存用户当前登陆Token
     * @param userId 用户ID
     * @param token token
     */
    public void addToken(Long userId, String token) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(userIdToKey(userId), token, jwtConfig.getExpireHour(), TimeUnit.HOURS);
    }

    /**
     * 删除某用户的Token
     * @param userId 用户ID
     */
    public void removeToken(Long userId) {
        if(Boolean.FALSE.equals(redisTemplate.delete(userIdToKey(userId)))) {
            throw new SystemException("Redis删除异常");
        }
    }

    /**
     * 获得某用户Token
     * @param userId 用户ID
     * @return token
     */
    public String getToken(Long userId) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        return operations.get(userIdToKey(userId));
    }

    /**
     * 获得当前用户Token剩余有效期
     * @param userId 用户ID
     * @return 剩余有效期（分钟）
     */
    public Long getValidMinutes(Long userId) {
        return redisTemplate.getExpire(userIdToKey(userId), TimeUnit.MINUTES);
    }
}
