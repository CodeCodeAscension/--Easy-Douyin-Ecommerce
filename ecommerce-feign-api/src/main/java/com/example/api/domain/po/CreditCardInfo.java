package com.example.api.domain.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "银行卡信息")
public class CreditCardInfo {
    @ApiModelProperty("银行卡号")
    private String creditCardNumber;
    @ApiModelProperty("卡验证值")
    private Integer creditCardCvv;
    @ApiModelProperty("到期年份")
    private Integer creditCardExpirationYear;
    @ApiModelProperty("到期月份")
    private Integer creditCardExpirationMonth;
}
