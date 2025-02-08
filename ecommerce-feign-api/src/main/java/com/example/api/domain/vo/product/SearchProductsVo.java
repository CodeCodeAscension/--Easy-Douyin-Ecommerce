package com.example.api.domain.vo.product;

import com.example.api.domain.po.Product;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "查询商品结果")
public class SearchProductsVo {
    @ApiModelProperty("查询结果")
    List<Product> results;
}
