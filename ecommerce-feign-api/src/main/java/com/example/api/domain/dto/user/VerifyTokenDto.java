package com.example.api.domain.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "验证令牌请求")
public class VerifyTokenDto {
    @ApiModelProperty("待验证的令牌")
    String token;
}
