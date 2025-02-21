package com.example.user.domain.dto;

import com.example.user.enums.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "用户注销DTO")
public class LogoffDto {
    @Schema(description = "用户状态（1封禁2注销）", type = "Integer")
    @NotNull
    UserStatus status;
    @Schema(description = "原因")
    String reason;
}
