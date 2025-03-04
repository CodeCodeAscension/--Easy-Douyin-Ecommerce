package com.example.cart.domain.po;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.example.api.enums.OrderStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 购物车物品数据库
 * </p>
 *
 * @author author
 * @since 2025-02-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("cart_item")
@Schema(description = "购物车物品数据库")
public class CartItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "购物车物品ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "商品ID")
    @TableField("product_id")
    private Long productId;

    @Schema(description = "商品数量")
    @TableField("quantity")
    private Integer quantity;

    @Schema(description = "状态（0待下单，1已删除）")
    @TableLogic
    private Integer status;

    @Schema(description = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;

    @Schema(description = "购物车ID")
    @TableField("cart_id")
    private Long cartId;

}
