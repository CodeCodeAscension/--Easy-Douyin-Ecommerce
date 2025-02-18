package com.example.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.example.common.exception.BadRequestException;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;

@AllArgsConstructor
@Getter
public enum UserPower {
    USER(0,"普通用户"),
    ADMIN(1,"管理员");

    @EnumValue
    private final int code;
    private final String description;

    /**
     * 将权限值转换为对应的枚举对象
     * @param code 权限值
     * @return UserPower枚举对象
     */
    public static UserPower getByCode(int code) {
        for (UserPower userPower : UserPower.values()) {
            if (userPower.getCode() == code) {
                return userPower;
            }
        }
        throw new BadRequestException("未知的用户权限");
    }
}
