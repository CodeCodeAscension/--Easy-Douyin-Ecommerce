package com.example.product.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NonNull;

@Data
@Schema(description = "增加商品销量DTO")
public class AddProductSoldDto {

    @NonNull
    @Schema(description = "商品ID")
    private Long productId;

    @NonNull
    @Schema(description = "增加销量")
    private Integer addSold;
}
