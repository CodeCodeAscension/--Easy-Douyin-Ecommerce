package com.example.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 *     JWT令牌配置信息
 * </p>
 * @author vlsmb
 */
@Component
@Data
@ConfigurationProperties(prefix = "ecommerce.jwt")
public class JwtConfig {
    private String key;
    private Integer expireHour;
    private Integer refreshExpireDay;
}
