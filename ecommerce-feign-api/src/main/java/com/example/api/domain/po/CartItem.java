package com.example.api.domain.po;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "购物车物品")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    @Schema(description = "购物车ID")
    @NotNull
    private Long cartItemId;
    @Schema(description = "商品ID")
    @NotNull
    private Long productId;
    @Schema(description = "商品数量")
    @NotNull
    private Integer quantity;
}
