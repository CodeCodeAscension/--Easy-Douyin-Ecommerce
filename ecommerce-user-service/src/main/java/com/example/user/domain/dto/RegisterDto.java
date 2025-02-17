package com.example.user.domain.dto;

//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
//@ApiModel(description = "注册表单")
public class RegisterDto {
//    @ApiModelProperty(value = "用户邮箱", required = true)
    @NotBlank
    @Email
    private String email;

//    @ApiModelProperty(value = "密码",required = true)
    @NotBlank
    private String password;

//    @ApiModelProperty(value = "用户再次确认的密码",required = true)
    @NotBlank
    private String confirmPassword;
}
