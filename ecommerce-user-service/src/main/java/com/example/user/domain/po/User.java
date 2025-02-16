package com.example.user.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.user.enums.UserPower;
import com.example.user.enums.UserStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "用户信息实体类")
public class User {
    @ApiModelProperty("用户ID")
    @TableId(type = IdType.ASSIGN_ID)
    private Long userId;

    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("密码（BCrypt密文）")
    private String password;
    @ApiModelProperty("用户权限")
    private UserPower power;
    @ApiModelProperty("用户名称")
    private String userName;
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("城市")
    private String city;
    @ApiModelProperty("省份")
    private String province;
    @ApiModelProperty("国家")
    private String country;
    @ApiModelProperty("邮编")
    private String zipCode;
    @ApiModelProperty("偏好货币")
    private String userCurrency;
    @ApiModelProperty("账号状态")
    private UserStatus status;
    @ApiModelProperty("注销或封禁原因")
    private String disableReason;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
