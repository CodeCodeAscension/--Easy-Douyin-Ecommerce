package com.example.api.domain.vo.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "支付结果信息")
public class ChargeVo {
    @Schema(description = "预交易ID")
    private String preTransactionId;
    @Schema(description = "交易ID")
    private String transactionId;
}
