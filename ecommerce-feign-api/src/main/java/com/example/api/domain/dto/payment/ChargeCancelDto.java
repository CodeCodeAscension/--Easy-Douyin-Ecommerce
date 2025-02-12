package com.example.api.domain.dto.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "定时取消支付请求")
public class ChargeCancelDto {
    @ApiModelProperty("交易ID")
    private String transactionId;
    @ApiModelProperty("是否设置定时取消（可以取消上次的定时）")
    private Boolean status;
    @ApiModelProperty("交易过多少分钟后自动取消（0为立即取消）")
    private Integer cancelAfterMinutes;
}
