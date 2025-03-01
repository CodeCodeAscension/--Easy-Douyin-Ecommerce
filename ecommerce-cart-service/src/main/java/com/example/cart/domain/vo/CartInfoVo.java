package com.example.cart.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "购物车信息视图对象")
public class CartInfoVo {

    @Schema(description = "购物车ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "状态（0待支付，1已支付，2已删除）")
    private Integer status;

    @Schema(description = "购物车中的商品信息列表")
    private List<CartItemInfo> cartItems;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}
