package com.example.api.domain.dto.checkout;

import com.example.api.domain.po.Address;
import com.example.api.domain.po.CreditCardInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "结算请求")
public class CheckoutDto {
    @ApiModelProperty("用户ID")
    private Long userId;
    @ApiModelProperty("名字")
    private String firstName;
    @ApiModelProperty("姓氏")
    private String lastName;
    @ApiModelProperty("电子邮箱")
    private String email;
    @ApiModelProperty("收件地址")
    private Address address;
    @ApiModelProperty("银行卡")
    private CreditCardInfo creditCard;
}
