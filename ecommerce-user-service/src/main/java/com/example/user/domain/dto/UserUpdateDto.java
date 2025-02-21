package com.example.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户信息更新DTO")
public class UserUpdateDto {
    @Schema(description = "用户名")
    private String userName;
    @Schema(description = "手机号")
    private String phone;
    @Schema(description = "城市")
    private String city;
    @Schema(description = "省份")
    private String province;
    @Schema(description = "国家")
    private String country;
    @Schema(description = "邮编")
    private String zipCode;
    @Schema(description = "用户使用的货币")
    private String userCurrency;
}
