package com.example.api.domain.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "查询商品请求")
public class SearchProductsDto {
    @ApiModelProperty("第几页")
    private Integer page;
    @ApiModelProperty("每页数量")
    private Long pageSize;
    @ApiModelProperty("商品名称")
    private String productName;
    @ApiModelProperty("商品价格下限")
    private Float priceLow;
    @ApiModelProperty("商品价格上限")
    private Float priceHigh;
    @ApiModelProperty("销量")
    private Integer stock;
    @ApiModelProperty("库存")
    private Integer sold;
    @ApiModelProperty("商家名称")
    private String merchantName;
    @ApiModelProperty("类别")
    private String categoryName;
}
