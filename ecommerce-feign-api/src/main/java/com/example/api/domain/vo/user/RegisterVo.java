package com.example.api.domain.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "新注册用户的ID")
public class RegisterVo {
    @ApiModelProperty("用户ID")
    private Long userId;
}
