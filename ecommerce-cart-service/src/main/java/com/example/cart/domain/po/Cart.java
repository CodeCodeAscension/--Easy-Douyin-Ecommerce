package com.example.cart.domain.po;

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
 * 购物车信息数据库
 * </p>
 *
 * @author author
 * @since 2025-02-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("cart")
@ApiModel(value="Cart对象", description="购物车信息数据库")
public class Cart implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "购物车ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

//    @ApiModelProperty(value = "购物车的商品ID，是一个json列表")
//    private String cartItems;

    @ApiModelProperty(value = "状态（0待支付，1已支付，2已删除）")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


}
