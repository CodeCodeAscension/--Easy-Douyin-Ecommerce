package com.example.api.domain.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "查询商品请求")
public class SearchProductsDto {
    @ApiModelProperty("条件")
    private String query;
}
