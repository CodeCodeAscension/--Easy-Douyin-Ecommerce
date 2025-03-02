package com.example.product.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.example.common.exception.BadRequestException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductStatusEnum {
    PUT_ON(0, "上架"),
    PUT_OFF(1,"下架");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String description;

    @JsonCreator
    public static ProductStatusEnum fromCode(Integer code) {
        for (ProductStatusEnum productStatusEnum : ProductStatusEnum.values()) {
            if (productStatusEnum.getCode().equals(code)) {
                return productStatusEnum;
            }
        }
        throw new BadRequestException("无法将枚举值转化为商品状态对象");
    }
}
