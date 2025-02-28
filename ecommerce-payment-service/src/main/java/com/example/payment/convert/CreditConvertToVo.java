package com.example.payment.convert;

import com.example.payment.domain.po.Credit;
import com.example.payment.domain.vo.CreditVo;
import lombok.Data;


public class CreditConvertToVo {
    public static CreditVo convertToVo(Credit credit) {
        return CreditVo.builder()
                .cardNumber(credit.getCardNumber())
                .cardCvv(credit.getCardCvv())
                .userId(credit.getUserId())
                .balance(credit.getBalance())
                .expireDate(credit.getExpireDate())
                .status(credit.getStatus())
                .createTime(credit.getCreateTime())
                .updateTime(credit.getUpdateTime())
                .build();
    }
}
