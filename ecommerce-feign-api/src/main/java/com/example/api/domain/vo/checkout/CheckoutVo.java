package com.example.api.domain.vo.checkout;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "订单结算结果")
public class CheckoutVo {
    @ApiModelProperty("订单ID")
    private String orderId;
    @ApiModelProperty("交易ID")
    private String transactionId;
}
