package com.example.user.domain.vo;

import com.example.user.enums.UserStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "用户详细信息")
public class UserInfoVo {
    @Schema(description = "用户ID")
    private Long userId;
    @Schema(description = "用户邮箱")
    private String email;
    @Schema(description = "用户名")
    private String userName;
    @Schema(description = "电话号")
    private String phone;
    @Schema(description = "所在城市")
    private String city;
    @Schema(description = "所在省份")
    private String province;
    @Schema(description = "国家")
    private String country;
    @Schema(description = "邮政编码")
    private String zipCode;
    @Schema(description = "偏好货币")
    private String userCurrency;
    @Schema(description = "状态（0正常，1封禁，2注销）")
    private UserStatusEnum status;
    @Schema(description = "注销或封禁原因")
    private String disableReason;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "修改时间")
    private LocalDateTime updateTime;
}
