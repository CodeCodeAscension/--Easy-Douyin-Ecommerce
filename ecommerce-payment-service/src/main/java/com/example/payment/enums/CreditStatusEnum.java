package com.example.payment.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.example.common.exception.BadRequestException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CreditStatusEnum {
    NORMAL(0,"正常"),
    DISABLE(1,"禁用"),
    EXPIRED(2,"过期");

    @EnumValue
    @JsonValue
    private final Integer id;
    private final String description;

    @JsonCreator
    public static CreditStatusEnum fromId(Integer id) {
        for (CreditStatusEnum creditStatusEnum : CreditStatusEnum.values()) {
            if (creditStatusEnum.getId().equals(id)) {
                return creditStatusEnum;
            }
        }
        throw new BadRequestException("无法从枚举值转换成银行卡状态对象");
    }
}
