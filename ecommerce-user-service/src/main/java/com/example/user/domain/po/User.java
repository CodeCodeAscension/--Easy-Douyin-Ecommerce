package com.example.user.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.auth.enums.UserPower;
import com.example.user.enums.UserStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "用户信息实体类")
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Schema(description = "用户ID")
    @TableId(type = IdType.ASSIGN_ID)
    private Long userId;

    @Schema(description = "用户邮箱")
    private String email;
    @Schema(description = "密码（BCrypt密文）")
    private String password;
    @Schema(description = "用户权限")
    private UserPower power;
    @Schema(description = "用户名称")
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
    @Schema(description = "偏好货币")
    private String userCurrency;
    @Schema(description = "账号状态")
    private UserStatusEnum status;
    @Schema(description = "注销或封禁原因")
    private String disableReason;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
