package com.example.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.example.common.exception.BadRequestException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserStatusEnum {
    NORMAL(0,"正常"),
    BANNED(1,"封禁"),
    TERMINATED(2,"注销");

    @EnumValue
    @JsonValue
    private final int code;
    private final String description;

    @JsonCreator
    public static UserStatusEnum fromCode(int code) {
        for (UserStatusEnum userStatusEnum : UserStatusEnum.values()) {
            if (userStatusEnum.code == code) {
                return userStatusEnum;
            }
        }
        throw new BadRequestException("用户状态枚举值不正确");
    }
}
