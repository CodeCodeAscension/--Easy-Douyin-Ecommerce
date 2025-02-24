package com.example.product.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NonNull;

@Data
@Schema(description = "减少商品库存DTO")
public class DecProductDto {

    @NonNull
    @Schema(description = "商品ID")
    private Long productId;

    @NonNull
    @Schema(description = "减少库存数量")
    private Integer decStock;
}
