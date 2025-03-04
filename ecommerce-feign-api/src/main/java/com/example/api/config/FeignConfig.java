package com.example.api.config;

import com.example.common.exception.UnauthorizedException;
import com.example.common.util.UserContextUtil;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.example.api.client.fallback")
public class FeignConfig {
    @Bean
    public Logger.Level feignLogLevel(){
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor userInfoRequestInterceptor(){
        return template -> {
            try {
                Long userId = UserContextUtil.getUserId();
                template.header("X-User-Id", userId.toString());
            } catch (UnauthorizedException e) {
                return;
            }
        };
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new EcommerceErrorCode();
    }
}
