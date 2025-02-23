package com.example.payment.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("credit")
@Builder
public class Transaction implements Serializable{

    private static final long serialVersionUID = 1L;

    // 交易ID
    @TableId(value = "transaction_id", type = IdType.ASSIGN_UUID)
    private String transId;

    private String preTransId;

    private Long userId;

    private String orderId;

    // 使用的银行卡id
    private String creditId;

    // 交易金额
    private Float amount;

    // 交易状态（0成功，1失败）
    private Integer status;

    // 失败原因
    private String reason;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    // 逻辑删除
    @TableLogic
    private Integer deleted;
}
