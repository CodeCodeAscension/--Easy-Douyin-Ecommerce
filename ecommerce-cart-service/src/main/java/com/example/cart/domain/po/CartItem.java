package com.example.cart.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value="CartItem对象", description="购物车物品数据库")
public class CartItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "购物车物品ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "商品ID")
    @TableField("product_id")
    private Long productId;

    @ApiModelProperty(value = "商品数量")
    @TableField("quantity")
    private Integer quantity;

    @ApiModelProperty(value = "状态（0待支付，1已支付，2已删除）")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "购物车ID")
    @TableField("cart_id")
    private Long cartId;


}
