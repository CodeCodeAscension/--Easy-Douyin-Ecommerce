package com.example.api.client.fallback;

import com.example.api.client.UserClient;
import com.example.api.domain.vo.user.UserInfoVo;
import com.example.common.domain.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserClientFallBack implements FallbackFactory<UserClient> {
    @Override
    public UserClient create(Throwable cause) {
        return new UserClient() {
            @Override
            public ResponseResult<UserInfoVo> getUserInfo() {
                log.error("user-service-exception:getUserInfo, "+cause.getMessage());
                return ResponseResult.errorFeign(cause);
            }
        };
    }
}
