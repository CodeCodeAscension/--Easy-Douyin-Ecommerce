package com.example.api.domain.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户登录请求")
public class LoginDto {
    @ApiModelProperty("邮件地址")
    private String email;
    @ApiModelProperty("密码")
    private String password;
}
