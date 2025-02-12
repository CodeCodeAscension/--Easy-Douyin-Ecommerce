package com.example.api.client;

import com.example.api.client.fallback.UserClientFallBack;
import com.example.api.domain.vo.user.UserInfoVo;
import com.example.common.domain.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "user-service", fallbackFactory = UserClientFallBack.class)
public interface UserClient {
    // 获得当前登录用户的信息
    @GetMapping("/api/v1/users")
    ResponseResult<UserInfoVo> getUserInfo();
}
