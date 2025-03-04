package com.example.product.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "增加商品库存DTO")
@NoArgsConstructor
@AllArgsConstructor
public class AddProductDto {
    @NotNull
    @Schema(description = "商品ID")
    private Long productId;
    @NotNull
    @Min(1)
    @Schema(description = "增加库存数量")
    private Integer addStock;
}
