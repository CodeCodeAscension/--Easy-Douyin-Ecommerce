package com.example.api.domain.dto.order;

import com.example.api.domain.po.CartItem;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@Schema(description = "下订单DTO")
public class PlaceOrderDto {
    @Schema(description = "使用的货币")
    @NotEmpty
    private String userCurrency;
    @Schema(description = "地址")
    @NotNull
    private Long addressId;
    @Schema(description = "电子邮件")
    @Email
    @NotBlank
    private String email;
    @Schema(description = "下单的商品")
    @NotEmpty
    @NotNull
    @Valid
    private List<CartItem> cartItems;
}
