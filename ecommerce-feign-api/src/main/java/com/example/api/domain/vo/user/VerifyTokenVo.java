package com.example.api.domain.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "令牌验证结果")
public class VerifyTokenVo {
    @ApiModelProperty("令牌是否有效")
    private Boolean res;
}
