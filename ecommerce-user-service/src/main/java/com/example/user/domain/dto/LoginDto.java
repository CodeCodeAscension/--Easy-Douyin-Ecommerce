package com.example.user.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@ApiModel(description = "登陆表单")
public class LoginDto {
    @ApiModelProperty(value = "用户注册时的邮箱", required = true)
    @NotBlank
    @Email
    private String email;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank
    private String password;
}
