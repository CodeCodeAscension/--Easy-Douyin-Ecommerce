package com.example.api.domain.vo.product;

import com.example.api.domain.po.Product;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "查询商品信息结果")
public class ListProductsVo {
    @ApiModelProperty("商品信息")
    List<Product> products;
}
