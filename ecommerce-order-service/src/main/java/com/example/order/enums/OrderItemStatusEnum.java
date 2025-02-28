package com.example.order.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public enum OrderItemStatusEnum {
    PENDING(0, "待支付"),
    PAID(1, "已支付"),
    DELETED(2, "已删除");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String description;

    @NotNull
    @JsonCreator
    public static OrderItemStatusEnum fromCode(Integer code) {
        for (OrderItemStatusEnum status : OrderItemStatusEnum.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status code: " + code);
    }
}
