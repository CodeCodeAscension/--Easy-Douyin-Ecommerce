package com.example.api.domain.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "订单商品实体类")
public class OrderItem {
    @ApiModelProperty("购物车商品")
    private CartItem item;
    @ApiModelProperty("商品价格")
    private Float cost;
}
