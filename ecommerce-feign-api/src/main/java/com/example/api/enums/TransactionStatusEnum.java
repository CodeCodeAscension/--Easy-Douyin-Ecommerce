package com.example.api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.example.common.exception.BadRequestException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionStatusEnum {
    WAIT_FOR_CONFIRM(0, "待确认"),
    PAY_SUCCESS(1,"支付成功"),
    PAY_FAIL(2,"支付失败"),
    CANCELED(3,"已取消");

    @EnumValue
    @JsonValue
    private final int code;
    private final String description;

    @JsonCreator
    public static TransactionStatusEnum fromCode(int code) {
        for (TransactionStatusEnum transactionStatusEnum : TransactionStatusEnum.values()) {
            if (transactionStatusEnum.getCode() == code) {
                return transactionStatusEnum;
            }
        }
        throw new BadRequestException("支付状态未知的枚举值");
    }
}
