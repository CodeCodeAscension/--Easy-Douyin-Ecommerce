package com.example.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "修改某个用户权限DTO")
public class UpdatePowerDto {
    @Schema(description = "用户ID")
    @NotNull
    private Long userId;
    @Schema(description = "是否为管理员")
    @NotNull
    private Boolean status;
}
