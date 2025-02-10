package com.example.api.config;

import com.example.common.util.UserContextUtil;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;

public class FeignConfig {
    @Bean
    public Logger.Level feignLogLevel(){
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor userInfoRequestInterceptor(){
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                Long userId = UserContextUtil.getUserId();
                if(userId == null) {
                    return;
                }
                template.header("X-User-Id", userId.toString());
            }
        };
    }
}
