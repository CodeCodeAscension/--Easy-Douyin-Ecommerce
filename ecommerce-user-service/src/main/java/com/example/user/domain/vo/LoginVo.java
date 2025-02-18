package com.example.user.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "返回用户登陆的信息")
public class LoginVo {
    @Schema(description = "用户ID")
    private Long userId;
    @Schema(description = "用户Token")
    private String token;
}
