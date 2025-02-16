package com.example.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 *     用户微服务的一些配置属性
 * </p>
 * @author vlsmb
 */
@Data
@ConfigurationProperties(prefix = "ecommerce.user-service")
@Component
public class UserServiceConfig {
    private Integer pwdLength;
}
