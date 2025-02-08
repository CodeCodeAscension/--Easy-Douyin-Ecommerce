package com.example.user.controller;

import com.example.api.domain.dto.user.LoginDto;
import com.example.api.domain.dto.user.RegisterDto;
import com.example.api.domain.vo.user.LoginVo;
import com.example.api.domain.vo.user.RegisterVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "用户相关接口")
public class UserController {

    @PostMapping("/login")
    @ApiOperation("登录接口")
    public LoginVo login(@RequestBody LoginDto loginDto) {
        // 临时测试
        LoginVo loginVo = new LoginVo();
        loginVo.setUserId(233333L);
        System.out.println(loginDto);
        return loginVo;
    }

    @PostMapping("/register")
    @ApiOperation("注册接口")
    public RegisterVo register(@RequestBody RegisterDto registerDto) {
        // 临时测试
        RegisterVo registerVo = new RegisterVo();
        registerVo.setUserId(233333L);
        System.out.println(registerDto);
        return registerVo;
    }
}
