package com.example.payment.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

/**
 * 交易定时取消类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChargeCancelDto {

    // 交易id
    @NotEmpty
    private String transactionId;

    // 是否设置定时取消（可以取消上次的定时）
    @NonNull
    private Boolean status;

    // 交易过多少分钟后自动取消（0为立即取消）
    @NonNull
    private Integer cancelAfterMinutes;
}
