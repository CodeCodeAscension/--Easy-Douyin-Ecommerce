package com.example.order.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.io.Serial;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.example.api.enums.OrderStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单信息数据库
 * </p>
 *
 * @author author
 * @since 2025-02-28
 */
@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("`order`")
@Schema(description = "订单信息数据库")
public class Order implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "订单ID")
    @TableId(value = "order_id", type = IdType.ASSIGN_ID)
    private String orderId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户使用的货币")
    private String userCurrency;

    @Schema(description = "用户收货地址ID")
    private Long addressId;

    @Schema(description = "用户邮箱")
    private String email;

    @Schema(description = "状态（0待支付，1已支付，2已取消）")
    private OrderStatusEnum status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "支付时间")
    private LocalDateTime payTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "逻辑删除")
    private Integer deleted;

}
