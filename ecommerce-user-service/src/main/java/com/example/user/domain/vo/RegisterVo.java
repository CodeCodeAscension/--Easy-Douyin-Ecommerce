package com.example.user.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户注册结果")
public class RegisterVo {
    @Schema(description = "用户ID")
    private Long userId;
}
