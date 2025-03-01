package com.example.cart.enums;


import com.baomidou.mybatisplus.annotation.EnumValue;
import com.example.common.exception.BadRequestException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;


@RequiredArgsConstructor
@Getter
public enum OrderStatusEnum {
    UNPAID(0, "待支付"),
    PAID(1, "已支付"),
    DELETED(2, "已删除");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String description;

    // 根据 code 获取枚举值
    @NotNull
    @JsonCreator
    public static OrderStatusEnum getByCode(Integer code) {
        for (OrderStatusEnum status : OrderStatusEnum.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new BadRequestException("Invalid status code: " + code);
    }
}
