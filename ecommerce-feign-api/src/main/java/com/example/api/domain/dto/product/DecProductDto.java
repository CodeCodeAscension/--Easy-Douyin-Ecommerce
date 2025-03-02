package com.example.api.domain.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "减少商品库存DTO")
public class DecProductDto {
    @NotNull
    @Schema(description = "商品ID")
    private Long productId;
    @NotNull
    @Min(1)
    @Schema(description = "减少库存数量")
    private Integer decStock;
}
