package com.example.payment.domain.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreditDto {
    @NotBlank(message = "卡号不能为空")
    private String cardNumber;

    @NotNull(message = "CVV不能为空")
    private Integer cvv;

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotNull(message = "余额不能为空")
    @DecimalMin(value = "0.0", message = "余额不能小于0")
    private Float balance;

    @NotNull(message = "过期日期不能为空")
    private LocalDate expireDate;

    private Integer status;

    private Integer deleted;


}