package com.example.api.domain.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "用户详细信息")
public class UserInfoVo {
    @ApiModelProperty("用户ID")
    private Long userId;
    @ApiModelProperty("用户邮箱")
    private String email;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("电话号")
    private String phone;
    @ApiModelProperty("所在城市")
    private String city;
    @ApiModelProperty("所在省份")
    private String province;
    @ApiModelProperty("国家")
    private String country;
    @ApiModelProperty("邮政编码")
    private String zipCode;
    @ApiModelProperty("偏好货币")
    private String userCurrency;
    @ApiModelProperty("状态（0正常，1封禁，2注销）")
    private Integer status;
    @ApiModelProperty("注销或封禁原因")
    private String disableReason;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;
}
