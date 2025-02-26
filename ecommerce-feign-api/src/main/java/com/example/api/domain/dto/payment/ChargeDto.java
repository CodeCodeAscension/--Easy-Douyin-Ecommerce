package com.example.api.domain.dto.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "收费请求DTO")
public class ChargeDto {
    @NotBlank(message = "订单不能为空")
    @Schema(description = "订单ID")
    private String orderId;

    @NotBlank(message = "银行卡号不能为空")
    @Schema(description = "银行卡ID")
    private String creditId;

}
