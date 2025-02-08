package com.example.api.domain.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "获取商品信息请求")
public class GetProductDto {
    @ApiModelProperty("商品ID")
    private Long id;
}
