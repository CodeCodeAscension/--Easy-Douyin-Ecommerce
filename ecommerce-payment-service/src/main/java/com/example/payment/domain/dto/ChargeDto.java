package com.example.payment.domain.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChargeDto {

    // 订单id
    @NotEmpty
    private String orderId;

    // 银行卡id
    @NotEmpty
    private String creditId;

    // 交易金额
    @NonNull
    @DecimalMin(value = "0.0", message = "交易金额不能小于0")
    private Float amount;
}
