package com.example.payment.convert;

import com.example.payment.domain.dto.CreditDto;
import com.example.payment.domain.po.Credit;

import java.time.LocalDateTime;

public class CreditDtoConvertToPo {
    public static Credit convertToPo(Long userId, CreditDto dto) {
        return Credit.builder()
                .cardNumber(dto.getCardNumber())
                .cvv(dto.getCvv())
                .userId(userId)
                .balance(dto.getBalance())
                .expireDate(dto.getExpireDate())
                .status(0)
                .version(0)
                .deleted(0)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
    }
}
