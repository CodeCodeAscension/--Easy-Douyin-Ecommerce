package com.example.user.controller.v1;

import com.example.api.domain.vo.user.UserInfoVo;
import com.example.common.domain.ResponseResult;
import com.example.common.domain.ResultCode;
import com.example.common.exception.UnauthorizedException;
import com.example.common.util.UserContextUtil;
import com.example.user.service.UserService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *    用户信息数据库 前端控制器
 * </p>
 * @author vlsmb
 */
@RestController
@RequestMapping("/api/v1/users")
//@Api(tags = "用户相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
//    @ApiOperation("获取用户信息")
    public ResponseResult<UserInfoVo> getUserInfo() {
        Long userId = UserContextUtil.getUserId();
        if (userId == null) {
            throw new UnauthorizedException("用户未登录");
        }
        UserInfoVo userInfoVo = userService.getUserInfo(userId);
        if(userInfoVo == null) {
            return ResponseResult.error(ResultCode.BAD_REQUEST, "用户信息不存在");
        }
        return ResponseResult.success(userInfoVo);
    }
}
