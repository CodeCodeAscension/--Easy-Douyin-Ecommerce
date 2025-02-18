package com.example.api.domain.vo.order;

import com.example.api.domain.po.OrderResult;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
//@ApiModel(description = "创建订单结果")
public class PlaceOrderVo {
//    @ApiModelProperty("订单信息")
    private OrderResult order;
}
