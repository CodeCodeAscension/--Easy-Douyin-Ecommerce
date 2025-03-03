package com.example.payment.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 定义一个String, String, Long类型的RedisHash操作方法
 */
@Component
@RequiredArgsConstructor
public class RedisHashUtil {
    private final RedisTemplate<String, Object> redisTemplate;

    public void put(String key, String field, Long value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    public Long get(String key, String field) {
        Object o = redisTemplate.opsForHash().get(key, field);
        if (o == null) {
            return null;
        }
        try {
            return Long.parseLong(o.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public Set<String> keys(String key) {
        Set<Object> set = redisTemplate.opsForHash().keys(key);
        return set.stream().map(Object::toString).collect(Collectors.toSet());
    }

    public void delete(String key, String field) {
        redisTemplate.opsForHash().delete(key, field);
    }
}
