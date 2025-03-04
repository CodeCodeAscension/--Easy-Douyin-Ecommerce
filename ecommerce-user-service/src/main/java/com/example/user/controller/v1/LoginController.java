package com.example.user.controller.v1;

import com.example.auth.domain.UserClaims;
import com.example.common.domain.ResponseResult;
import com.example.common.domain.ResultCode;
import com.example.common.exception.BadRequestException;
import com.example.common.exception.UnauthorizedException;
import com.example.common.util.UserContextUtil;
import com.example.user.config.UserServiceConfig;
import com.example.user.domain.dto.LoginDto;
import com.example.user.domain.dto.RegisterDto;
import com.example.user.domain.po.User;
import com.example.user.domain.vo.LoginVo;
import com.example.user.domain.vo.RegisterVo;
import com.example.user.service.UserService;
import com.example.auth.util.JwtUtil;
import com.example.auth.util.TokenRedisUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *     用户登陆注册，前端控制器
 * </p>
 * @author vlsmb
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(name = "用户登陆接口",description = "用户登陆接口")
public class LoginController {

    private final UserServiceConfig userServiceConfig;
    private final UserService iUserService;
    private final JwtUtil jwtUtil;
    private final TokenRedisUtil redisUtil;

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public ResponseResult<RegisterVo> register(@Validated @RequestBody RegisterDto registerDto) {
        // 字符串去掉左侧右侧空格
        String password = registerDto.getPassword().trim();
        String confirmPassword = registerDto.getConfirmPassword().trim();

        // 检验密码是否符合规则
        if (!password.equals(confirmPassword)) {
            throw new BadRequestException("两次密码不一致");
        }
        if (password.length() < userServiceConfig.getPwdLength()) {
            throw new BadRequestException("密码的长度至少为"+userServiceConfig.getPwdLength()+"位！");
        }
        // 查询该邮箱是否注册过账号
        if(iUserService.findEnabledUserId(registerDto.getEmail()) != null) {
            throw new BadRequestException("该邮箱已经注册了账号！");
        }

        // 返回提示信息
        Long userId = iUserService.register(registerDto);
        RegisterVo registerVo = new RegisterVo();
        registerVo.setUserId(userId);
        return ResponseResult.success(registerVo);
    }

    /**
     * 生成并记录AccessToken和RefreshToken，供登陆和刷新接口使用
     * @param userClaims 用户claims对象
     * @return ResponseResult对象
     */
    private ResponseResult<LoginVo> generateTokens(UserClaims userClaims) {
        // 记录返回结果
        LoginVo loginVo = new LoginVo();
        loginVo.setUserId(userClaims.getUserId());
        // 生成AccessToken和RefreshToken
        userClaims.setIsRefreshToken(false);
        String accessToken = jwtUtil.generateAccessToken(userClaims);
        loginVo.setAccessToken(accessToken);
        userClaims.setIsRefreshToken(true);
        String refreshToken = jwtUtil.generateRefreshToken(userClaims);
        loginVo.setRefreshToken(refreshToken);

        // 保存到Redis里
        redisUtil.addToken(userClaims.getUserId(), accessToken, refreshToken);
        return ResponseResult.success(loginVo);
    }

    @PostMapping("/login")
    @Operation(summary = "用户登陆")
    public ResponseResult<LoginVo> login(@Validated @RequestBody LoginDto loginDto) {
        User user = iUserService.checkPassword(loginDto);
        if (user == null) {
            return ResponseResult.error(ResultCode.UNAUTHORIZED, "用户不存在或者密码错误");
        }
        UserClaims userClaims = new UserClaims();
        userClaims.setUserId(user.getUserId());
        userClaims.setUserPower(user.getPower().getCode());
        return generateTokens(userClaims);
    }

    @PostMapping("/logout")
    @Operation(summary = "用户登出")
    public ResponseResult<Object> logout() {
        Long userId = UserContextUtil.getUserId();
        redisUtil.removeToken(userId);
        return ResponseResult.success();
    }

    @PutMapping("/refresh")
    @Operation(summary = "使用RefreshToken刷新AccessToken")
    public ResponseResult<LoginVo> refresh(@RequestHeader("Refresh-Token") String refreshToken) {
        UserClaims userClaims = jwtUtil.verifyToken(refreshToken);
        if(!Boolean.TRUE.equals(userClaims.getIsRefreshToken())) {
            throw new UnauthorizedException("请使用RefreshToken访问本接口");
        }
        if(!refreshToken.equals(redisUtil.getRefreshToken(userClaims.getUserId()))) {
            throw new UnauthorizedException("RefreshToken已过期");
        }
        return generateTokens(userClaims);
    }
}
