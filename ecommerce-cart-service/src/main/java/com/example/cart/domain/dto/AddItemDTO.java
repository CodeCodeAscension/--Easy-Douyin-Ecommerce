package com.example.cart.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "添加购物车DTO")
public class AddItemDTO {

    @NotNull
    @Schema(description = "商品id")
    private Long productId;

    @NotNull
    @Min(value = 1, message = "至少要有一个商品")
    @Schema(description = "商品数量")
    private Integer quantity;

}
