package com.example.checkout.domain.dto;

import com.example.api.domain.dto.order.PlaceOrderDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "订单结算信息")
public class CheckoutDto {

    @NotNull
    @Schema(description = "购物车ID")
    private Long cartId;

    @Schema(description = "用户名字")
    @NotBlank
    private String firstname;

    @Schema(description = "用户姓氏")
    @NotBlank
    private String lastname;

    @Schema(description = "创建订单的信息")
    @NotNull
    @Valid
    private PlaceOrderDto placeOrderDto;

    @Schema(description = "银行卡ID")
    @NotBlank
    private String creditId;
}
