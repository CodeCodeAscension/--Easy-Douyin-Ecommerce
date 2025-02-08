package com.example.api.domain.dto.cart;

import com.example.api.domain.po.CartItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "添加商品请求")
public class AddItemDto {
    @ApiModelProperty("用户ID")
    private Long userId;
    @ApiModelProperty("购物车物品")
    private CartItem item;
}
