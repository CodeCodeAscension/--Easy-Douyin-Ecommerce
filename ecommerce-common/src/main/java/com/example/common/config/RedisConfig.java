package com.example.common.config;

import com.example.common.cache.MybatisRedisCache;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Arrays;
import java.util.List;

@Configuration
@ConditionalOnClass(RedisConnectionFactory.class)
public class RedisConfig {

    @Bean
    @ConditionalOnMissingBean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // 注入到MybatisCache里
        MybatisRedisCache.setRedisTemplate(template);
        return template;
    }

    @Value("${ecommerce.redis.cluster-addr}")
    private String clusterAddr;

    @Bean
    @ConditionalOnMissingBean
    public RedissonClient redissonClient() {
        // 获取配置中集群的位置
        List<String> clusters = Arrays.asList(clusterAddr.split(","));
        String[] array = clusters.stream().map("redis://"::concat).toArray(String[]::new);
        Config config = new Config();
        config.useClusterServers()
                .addNodeAddress(array);
        // 注入到MybatisCache里
        RedissonClient redisson = Redisson.create(config);
        MybatisRedisCache.setRedissonClient(redisson);
        return redisson;
    }
}
