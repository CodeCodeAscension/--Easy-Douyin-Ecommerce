package com.example.api.domain.vo.payment;

import com.example.api.enums.TransactionStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Schema(description = "交易信息")
public class TransactionInfoVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "交易ID")
    private String transactionId;
    @Schema(description = "预交易ID")
    private String preTransactionId;
    @Schema(description = "订单ID")
    private String orderId;
    @Schema(description = "银行卡ID")
    private String creditId;
    @Schema(description = "金额")
    private Float amount;
    @Schema(description = "支付状态")
    private TransactionStatusEnum status;
    @Schema(description = "原因")
    private String reason;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
