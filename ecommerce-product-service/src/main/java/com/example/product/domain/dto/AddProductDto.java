package com.example.product.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NonNull;

@Data
@Schema(description = "增加商品库存DTO")
public class AddProductDto {

    @NonNull
    @Schema(description = "商品ID")
    private Long productId;

    @NonNull
    @Schema(description = "增加库存数量")
    private Integer addStock;
}
