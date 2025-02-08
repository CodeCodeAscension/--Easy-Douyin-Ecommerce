package com.example.api.domain.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "购物车实体类")
public class Cart {
    @ApiModelProperty("用户ID")
    private Integer userId;
    @ApiModelProperty("购物车商品信息")
    private List<CartItem> items;
}
