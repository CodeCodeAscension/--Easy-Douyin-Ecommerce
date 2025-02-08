package com.example.api.domain.vo.cart;

import com.example.api.domain.po.Cart;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "获得购物车信息结果")
public class GetCartVo {
    @ApiModelProperty("购物车信息")
    Cart cart;
}
