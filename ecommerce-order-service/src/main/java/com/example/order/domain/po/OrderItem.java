package com.example.order.domain.po;

import java.io.Serial;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.example.api.enums.OrderStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单商品信息数据库
 * </p>
 *
 * @author author
 * @since 2025-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("order_item")
@Schema(description = "订单商品信息数据库")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "订单商品ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "所属订单ID")
    private String orderId;

    @Schema(description = "购物车itemID")
    private Long cartItemId;

    @Schema(description = "商品ID")
    private Long productId;

    @Schema(description = "商品数量")
    private Integer quantity;

    @Schema(description = "总消费")
    private Float cost;

    @Schema(description = "状态（0待支付，1已支付，2已取消）")
    private OrderStatusEnum status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "支付时间")
    private LocalDateTime payTime;

    @Schema(description = "修改时间")
    private LocalDateTime updateTime;

}
