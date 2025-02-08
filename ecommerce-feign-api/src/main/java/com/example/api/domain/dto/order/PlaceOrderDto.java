package com.example.api.domain.dto.order;

import com.example.api.domain.po.Address;
import com.example.api.domain.po.OrderItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "下订单请求")
public class PlaceOrderDto {
    @ApiModelProperty("用户ID")
    private Long userId;
    @ApiModelProperty("使用的货币")
    private String useCurrency;
    @ApiModelProperty("地址")
    private Address address;
    @ApiModelProperty("电子邮件")
    private String email;
    @ApiModelProperty("下单的商品")
    private List<OrderItem> orderItems;
}
