package com.example.api.domain.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "根据用户ID获取令牌请求")
public class DeliverTokenDto {
    @ApiModelProperty("用户ID")
    private Long userId;
}
