package com.example.user.controller.v1;

import com.example.common.domain.ResponseResult;
import com.example.common.domain.ResultCode;
import com.example.common.exception.BadRequestException;
import com.example.user.config.UserServiceConfig;
import com.example.user.domain.dto.LoginDto;
import com.example.user.domain.dto.RegisterDto;
import com.example.user.domain.po.User;
import com.example.user.domain.vo.LoginVo;
import com.example.user.domain.vo.RegisterVo;
import com.example.user.service.UserService;
import com.example.user.util.JwtUtil;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *     用户登陆注册，前端控制器
 * </p>
 * @author vlsmb
 */
@RestController
@RequestMapping("/api/v1/user")
//@Api(tags = "用户登陆注册控制器")
public class LoginController {

    @Autowired
    private UserServiceConfig userServiceConfig;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
//    @ApiOperation("用户注册")
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
        if(userService.findEnabledUserId(registerDto.getEmail()) != null) {
            throw new BadRequestException("该邮箱已经注册了账号！");
        }

        // 返回提示信息
        Long userId = userService.register(registerDto);
        RegisterVo registerVo = new RegisterVo();
        registerVo.setUserId(userId);
        return ResponseResult.success(registerVo);
    }

    @PostMapping("/login")
//    @ApiOperation("用户登录")
    public ResponseResult<LoginVo> login(@Validated @RequestBody LoginDto loginDto) {
        User user = userService.checkPassword(loginDto);
        if (user == null) {
            return ResponseResult.error(ResultCode.UNAUTHORIZED, "用户不存在或者密码错误");
        }

        // 记录返回结果
        LoginVo loginVo = new LoginVo();
        loginVo.setUserId(user.getUserId());
        // 生成JWT令牌
        String token = jwtUtil.generateToken(user);
        loginVo.setToken(token);
        return ResponseResult.success(loginVo);
    }
}
