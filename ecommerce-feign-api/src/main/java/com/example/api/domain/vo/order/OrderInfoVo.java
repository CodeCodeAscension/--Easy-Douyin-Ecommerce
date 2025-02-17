package com.example.api.domain.vo.order;

import com.example.api.domain.po.Address;
import com.example.api.domain.po.CartItem;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
//@ApiModel(description = "用户订单信息")
public class OrderInfoVo {
//    @ApiModelProperty("订单ID")
    private String orderId;
//    @ApiModelProperty("支付状态（0待支付，1已支付，2已取消）")
    private Integer status;
//    @ApiModelProperty("使用的货币")
    private String userCurrency;
//    @ApiModelProperty("地址")
    private Address address;
//    @ApiModelProperty("电子邮件")
    private String email;
//    @ApiModelProperty("下单的商品")
    private List<CartItem> cartItems;
//    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
//    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;
}
