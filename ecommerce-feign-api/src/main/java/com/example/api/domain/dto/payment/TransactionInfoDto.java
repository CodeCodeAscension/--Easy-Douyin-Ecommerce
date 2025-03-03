package com.example.api.domain.dto.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "查询交易信息DTO")
public class TransactionInfoDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Schema(description = "交易ID")
    private String transactionId;
    @Schema(description = "预交易ID")
    private String preTransactionId;
}
