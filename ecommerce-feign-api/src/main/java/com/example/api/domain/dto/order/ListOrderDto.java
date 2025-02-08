package com.example.api.domain.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "查看订单请求")
public class ListOrderDto {
    @ApiModelProperty("用户ID")
    private Long userId;
}
