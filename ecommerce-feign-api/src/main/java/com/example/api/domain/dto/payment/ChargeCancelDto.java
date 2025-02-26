package com.example.api.domain.dto.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "定时取消支付DTO")
public class ChargeCancelDto {
    @NotBlank(message = "预交易ID不能为空")
    @Schema(description = "预交易ID")
    private String preTransactionId;
    @NotNull
    @Schema(description = "是否设置定时取消（可以取消上次的定时）")
    private Boolean status;
    @NotNull
    @Schema(description = "交易过多少分钟后自动取消（0为立即取消）")
    private Integer cancelAfterMinutes;
}
