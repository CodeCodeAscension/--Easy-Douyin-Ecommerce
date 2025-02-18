package com.example.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserStatus {
    NORMAL(0,"正常"),
    BANNED(1,"封禁"),
    TERMINATED(2,"注销");

    @EnumValue
    private final int code;
    private final String description;
}
