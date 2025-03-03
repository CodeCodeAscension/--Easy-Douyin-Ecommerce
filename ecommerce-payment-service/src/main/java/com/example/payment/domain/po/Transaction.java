package com.example.payment.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import com.example.api.enums.TransactionStatusEnum;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("transaction")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    // 交易ID
    @TableId(value = "transaction_id", type = IdType.ASSIGN_UUID)
    private String transactionId;

    @TableField("pre_transaction_id")
    private String preTransactionId;

    private Long userId;

    private String orderId;

    // 使用的银行卡id
    private String creditId;

    // 交易金额
    private Float amount;

    // 交易状态（0成功，1失败）
    private TransactionStatusEnum status;

    // 失败原因
    private String reason;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    // 逻辑删除
    @TableLogic
    private Integer deleted;
}
