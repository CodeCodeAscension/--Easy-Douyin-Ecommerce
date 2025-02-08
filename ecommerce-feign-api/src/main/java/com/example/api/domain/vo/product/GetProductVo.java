package com.example.api.domain.vo.product;

import com.example.api.domain.po.Product;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "获取商品信息结果")
public class GetProductVo {
    @ApiModelProperty("商品信息")
    Product product;
}
