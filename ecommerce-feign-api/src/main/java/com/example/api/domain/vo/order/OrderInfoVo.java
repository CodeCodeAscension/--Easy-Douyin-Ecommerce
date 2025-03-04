package com.example.api.domain.vo.order;

import com.example.api.domain.po.CartItem;
import com.example.api.enums.OrderStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "用户订单信息")
public class OrderInfoVo {
    @Schema(description = "订单ID")
    private String orderId;
    @Schema(description = "支付状态（0待支付，1已支付，2已取消）")
    private OrderStatusEnum status;
    @Schema(description = "使用的货币")
    private String userCurrency;
    @Schema(description = "地址")
    private AddressInfoVo address;
    @Schema(description = "电子邮件")
    private String email;
    @Schema(description = "下单的商品")
    private List<CartItem> cartItems;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "修改时间")
    private LocalDateTime updateTime;
}
