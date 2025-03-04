package com.example.product.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "根据分类查询商品列表DTO")
public class ListProductsDto {
    @Schema(description = "页码")
    @Min(1)
    private Integer page = 1;

    @Schema(description = "每页数量")
    @Min(1)
    private Integer pageSize = 20;

    @Schema(description = "商品名称")
    private String categoryName;
}
