package com.example.api.domain.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(description = "订单信息实体类")
public class Order {
    @ApiModelProperty("订单商品信息")
    private List<OrderItem> orderItems;
    @ApiModelProperty("订单ID")
    private String orderId;
    @ApiModelProperty("用户ID")
    private Integer userId;
    @ApiModelProperty("使用的货币")
    private String useCurrency;
    @ApiModelProperty("收货地址")
    private Address address;
    @ApiModelProperty("邮件")
    private String email;
    @ApiModelProperty("订单创建时间")
    private LocalDateTime createAt;
}
