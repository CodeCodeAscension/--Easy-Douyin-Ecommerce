package com.example.api.domain.vo.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(description = "购物车信息")
public class CartInfoVo {
    @ApiModelProperty("购物车ID")
    private Long id;
    @ApiModelProperty("用户ID")
    private Long userId;
    @ApiModelProperty("状态（0待支付，1已支付，2已删除）")
    private Integer status;
    @ApiModelProperty("购物车所有商品信息")
    private List<CartItemInfoVo> cartItems;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;
}
