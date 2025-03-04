package com.example.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(description = "封禁用户DTO")
public class BannedDto {
    @Schema(description = "用户ID")
    @NotNull(message = "需要用户ID")
    private Long userId;
    @Schema(description = "封禁原因")
    @NotEmpty(message = "需要封禁原因")
    @Length(max = 200, message = "不能超过200字")
    private String reason;
}
