package com.example.api.domain.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户注册请求")
public class RegisterDto {
    @ApiModelProperty("电子邮件地址")
    private String email;
    @ApiModelProperty("用户密码")
    private String password;
    @ApiModelProperty("用户再次确认密码")
    private String confirmPassword;
}
