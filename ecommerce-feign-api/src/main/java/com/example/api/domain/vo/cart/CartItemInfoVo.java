package com.example.api.domain.vo.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "购物车商品信息")
public class CartItemInfoVo {
    @ApiModelProperty("购物车ID")
    private Long id;
    @ApiModelProperty("商品ID")
    private Long productId;
    @ApiModelProperty("商品数量")
    private Integer quantity;
    @ApiModelProperty("状态（0待支付，1已支付，2已删除）")
    private Integer status;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;
}
