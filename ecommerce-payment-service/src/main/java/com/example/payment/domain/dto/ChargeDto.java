package com.example.payment.domain.dto;

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
    private Float amount;
}
