package com.example.api.domain.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "标记订单已支付请求")
public class MarkOrderPaidDto {
    @ApiModelProperty("用户ID")
    private Long userId;
    @ApiModelProperty("订单ID")
    private String orderId;
}
