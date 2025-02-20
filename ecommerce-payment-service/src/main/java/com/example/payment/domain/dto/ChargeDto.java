package com.example.payment.domain.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChargeDto {

    // 订单id
    @NonNull
    private String orderId;

    // 银行卡id
    @NonNull
    private String creditId;

    // 交易金额
    @NonNull
    private Float amount;
}
