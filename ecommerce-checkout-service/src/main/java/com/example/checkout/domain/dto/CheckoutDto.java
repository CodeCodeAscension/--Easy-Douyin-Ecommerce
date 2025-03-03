package com.example.checkout.domain.dto;

import com.example.api.domain.dto.order.PlaceOrderDto;
import com.example.api.domain.po.Address;
import com.example.api.domain.po.CartItem;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@Schema(description = "订单结算信息")
public class CheckoutDto {

    @NonNull
    @Schema(description = "购物车ID")
    private Long cartId;

    @Schema(description = "用户名字")
    private String firstname;

    @Schema(description = "用户姓氏")
    private String lastname;

    @NotEmpty
    @Schema(description = "创建订单的信息")
    private PlaceOrderDto placeOrderDto;

    @NotEmpty
    @Schema(description = "银行卡ID")
    private String creditId;
}
