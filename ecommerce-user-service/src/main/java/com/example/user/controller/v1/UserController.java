package com.example.user.controller.v1;

import com.example.api.domain.vo.user.UserInfoVo;
import com.example.common.domain.ResponseResult;
import com.example.common.util.UserContextUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @GetMapping
    public ResponseResult<UserInfoVo> getUserInfo() {
        UserInfoVo userInfo = new UserInfoVo();
        Long userId = UserContextUtil.getUserId();
        userInfo.setUserId(userId);
        userInfo.setEmail("test@test.com");
//        int i = 1/0;
        return ResponseResult.success(userInfo);
    }
}
