package com.example.api.domain.dto.order;

//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
//@ApiModel(description = "让订单变成已支付")
public class MarkOrderPaidDto {
//    @ApiModelProperty("订单ID")
    private String orderId;
}
