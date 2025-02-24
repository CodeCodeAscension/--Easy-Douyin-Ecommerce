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
@Schema(description = "指定条件搜索商品DTO")
public class SearchProductsDto {

        @Schema(description = "页码")
        private Integer page;

        @Schema(description = "每页数量")
        private Integer pageSize;

        @Schema(description = "商品名称")
        private String productName;

        @Schema(description = "最低价格")
        private Float priceLow;

        @Schema(description = "最高价格")
        private Float priceHigh;

        @Schema(description = "销量")
        private Integer sold;

        @Schema(description = "库存")
        private Integer stoke;

        @Schema(description = "商家名称")
        private String merchantName;

        @Schema(description = "分类名称")
        private String categoryName;

}
