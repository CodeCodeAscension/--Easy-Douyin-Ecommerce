package com.example.api.domain.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "定时取消订单")
public class CancelOrderDto {
    @ApiModelProperty("订单ID")
    private String orderId;
    @ApiModelProperty("是否设置定时取消（可以取消上次的定时）")
    private Boolean status;
    @ApiModelProperty("订单过多少分钟后自动取消（0为立即取消）")
    private Integer cancelAfterMinutes;
}
