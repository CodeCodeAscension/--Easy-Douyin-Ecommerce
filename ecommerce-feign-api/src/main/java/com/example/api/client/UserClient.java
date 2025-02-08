package com.example.api.client;

import com.example.api.domain.dto.user.LoginDto;
import com.example.api.domain.dto.user.RegisterDto;
import com.example.api.domain.vo.user.LoginVo;
import com.example.api.domain.vo.user.RegisterVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("user-service")
public interface UserClient {
    @PostMapping("/register")
    RegisterVo register(@RequestBody RegisterDto registerDto);
    @PostMapping("/login")
    LoginVo login(@RequestBody LoginDto loginDto);
}
