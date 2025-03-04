package com.example.api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.example.common.exception.BadRequestException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderStatusEnum {
    WAIT_FOR_CONFIRM(0, "待确认"),
    WAIT_FOR_PAY(1, "待支付"),
    PAID(2, "已支付"),
    PAYMENT_FAIL(3,"支付失败"),
    CANCELED(4,"已取消");

    @JsonValue
    @EnumValue
    private final Integer code;
    private final String description;

    @JsonCreator
    public static OrderStatusEnum fromCode(Integer code) {
        for (OrderStatusEnum orderStatusEnum : OrderStatusEnum.values()) {
            if (orderStatusEnum.getCode().equals(code)) {
                return orderStatusEnum;
            }
        }
        throw new BadRequestException("无效的订单状态");
    }
}
