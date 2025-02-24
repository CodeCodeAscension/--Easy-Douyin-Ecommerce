package com.example.payment.convert;

import com.example.payment.domain.dto.CreditDto;
import com.example.payment.domain.po.Credit;
import com.example.payment.enums.CreditStatusEnum;

import java.time.LocalDateTime;

public class CreditDtoConvertToPo {
    public static Credit convertToPo(Long userId, CreditDto dto) {
        return Credit.builder()
                .cardNumber(dto.getCardNumber())
                .cardCvv(dto.getCvv())
                .userId(userId)
                .balance(dto.getBalance())
                .expireDate(dto.getExpireDate())
                .status(CreditStatusEnum.NORMAL)
                .version(0)
                .deleted(0)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
    }
}
