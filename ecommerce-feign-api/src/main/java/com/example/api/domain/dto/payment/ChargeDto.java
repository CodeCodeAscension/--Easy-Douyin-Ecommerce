package com.example.api.domain.dto.payment;

import com.example.api.domain.po.CreditCardInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "收费请求")
public class ChargeDto {
    @ApiModelProperty("支付金额")
    private Float amount;
    @ApiModelProperty("银行卡信息")
    private CreditCardInfo creditCard;
    @ApiModelProperty("订单ID")
    private String orderId;
    @ApiModelProperty("用户ID")
    private Integer userId;
}
