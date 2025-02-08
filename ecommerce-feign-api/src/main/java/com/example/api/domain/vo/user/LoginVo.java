package com.example.api.domain.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户登录结果")
public class LoginVo {
    @ApiModelProperty("用户ID")
    private Long userId;
}
