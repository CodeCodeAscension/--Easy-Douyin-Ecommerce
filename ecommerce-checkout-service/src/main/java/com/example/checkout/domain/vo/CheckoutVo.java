package com.example.checkout.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "结算信息")
public class CheckoutVo {

    @Schema(description = "订单ID")
    private String orderId;

    @Schema(description = "交易ID")
    private String transactionId;
}
