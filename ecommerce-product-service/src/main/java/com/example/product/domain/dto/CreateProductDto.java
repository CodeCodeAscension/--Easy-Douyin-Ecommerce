package com.example.product.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "创建商品DTO")
public class CreateProductDto {

    @NotEmpty
    @Schema(description = "商品名称")
    private String name;

    @Schema(description = "商品描述")
    private String description;

    @NonNull
    @Schema(description = "商品价格")
    private Float price;
}
