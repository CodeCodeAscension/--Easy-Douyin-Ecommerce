package com.example.user.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "注册DTO")
public class RegisterDto {
    @Schema(description = "注册邮箱")
    @NotBlank
    @Email
    private String email;

    @Schema(description = "密码")
    @NotBlank
    private String password;

    @Schema(description = "再次输入密码")
    @NotBlank
    private String confirmPassword;
}
