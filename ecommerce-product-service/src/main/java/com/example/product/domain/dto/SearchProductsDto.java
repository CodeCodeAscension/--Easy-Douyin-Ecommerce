package com.example.product.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Schema(description = "指定条件搜索商品DTO")
public class SearchProductsDto {
    @Schema(description = "页码")
    @Min(1)
    private Integer page = 1;

    @Schema(description = "每页数量")
    @Min(1)
    private Integer pageSize = 20;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "最低价格")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT)
    private Float priceLow;

    @Schema(description = "最高价格")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT)
    private Float priceHigh;

    @Schema(description = "销量")
    private Integer sold;

    @Schema(description = "库存")
    private Integer stock;

    @Schema(description = "商家名称")
    private String merchantName;

    @Schema(description = "分类名称")
    private String categoryName;
}
