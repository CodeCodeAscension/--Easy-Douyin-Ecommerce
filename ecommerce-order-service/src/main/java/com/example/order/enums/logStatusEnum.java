package com.example.order.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public enum logStatusEnum {
    NORMAL(0, "正常状态"),
    DEFENSE(1, "防御状态");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String description;

    @NotNull
    @JsonCreator
    public static logStatusEnum fromCode(Integer code) {
        for (logStatusEnum status : logStatusEnum.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status code: " + code);
    }
}
