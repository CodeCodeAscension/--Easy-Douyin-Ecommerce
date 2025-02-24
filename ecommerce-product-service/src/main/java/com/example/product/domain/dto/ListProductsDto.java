package com.example.product.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    private Integer page;

    @Schema(description = "每页数量")
    private Integer pageSize;

    @Schema(description = "商品名称")
    private String categoryName;
}
