package com.example.api.domain.po;

//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
//@ApiModel(description = "购物车物品实体类")
public class CartItem {
//    @ApiModelProperty("商品ID")
    private Long productId;
//    @ApiModelProperty("商品数量")
    private Integer quantity;
}
