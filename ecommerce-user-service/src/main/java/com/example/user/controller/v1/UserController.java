package com.example.user.controller.v1;

import com.example.api.domain.vo.user.UserInfoVo;
import com.example.auth.util.TokenRedisUtil;
import com.example.common.domain.ResponseResult;
import com.example.common.domain.ResultCode;
import com.example.common.exception.UnauthorizedException;
import com.example.common.util.UserContextUtil;
import com.example.user.domain.dto.LogoffDto;
import com.example.user.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *    用户信息数据库 前端控制器
 * </p>
 * @author vlsmb
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name="用户信息接口",description = "用户信息接口")
public class UserController {

    private final IUserService iUserService;
    private final TokenRedisUtil redisUtil;

    @GetMapping
    @Operation(summary = "获取用户信息")
    public ResponseResult<UserInfoVo> getUserInfo() {
        Long userId = UserContextUtil.getUserId();
        if (userId == null) {
            throw new UnauthorizedException("用户未登录");
        }
        UserInfoVo userInfoVo = iUserService.getUserInfo(userId);
        if(userInfoVo == null) {
            return ResponseResult.error(ResultCode.BAD_REQUEST, "用户信息不存在");
        }
        return ResponseResult.success(userInfoVo);
    }

    @DeleteMapping
    @Operation(summary = "用户注销或者封禁")
    public ResponseResult deleteUser(@RequestBody @Validated LogoffDto logoffDto) {
        Long userId = UserContextUtil.getUserId();
        if (userId == null) {
            throw new UnauthorizedException("用户未登录");
        }
        // 执行封禁操作
        iUserService.disableUser(userId, logoffDto);
        // 使Token失效
        redisUtil.removeToken(userId);
        return ResponseResult.success();
    }
}
