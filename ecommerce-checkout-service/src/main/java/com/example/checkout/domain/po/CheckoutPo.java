package com.example.checkout.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("checkout")
@Schema(description = "结算信息实体类")
public class CheckoutPo {

    @Schema(description = "结算ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "购物车ID")
    private Long cartId;

    @Schema(description = "订单ID，创建订单时填写")
    private String orderId;

    @Schema(description = "交易ID，创建交易时填写")
    private String transactionId;


    @Schema(description = "状态, 默认为0, 0，待确认，1待支付，2正在支付，3已支付，4交易失败，5已取消")
    private int status = 0;

    @Schema(description = "原因，交易失败或取消时填写")
    private String reason;

    @Schema(description = "用户名字")
    private String firstname;

    @Schema(description = "用户姓氏")
    private String lastname;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "逻辑删除")
    private int deleted = 0;

}
