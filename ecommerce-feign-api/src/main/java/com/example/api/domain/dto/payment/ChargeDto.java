package com.example.api.domain.dto.payment;

//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
//@ApiModel(description = "收费请求")
public class ChargeDto {
//    @ApiModelProperty("订单ID")
    private String orderId;
//    @ApiModelProperty("银行卡ID")
    private String creditId;
//    @ApiModelProperty("支付金额")
    private Float amount;
}
