package com.example.payment.domain.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreditUpdateDto {
    @NotBlank(message = "卡号不能为空")
    private String cardNumber;

    private Integer cvv;

    @NotNull(message = "用户ID不能为空")
    private Long userId;


    @DecimalMin(value = "0.0", message = "余额不能小于0")
    private Float balance;

    private LocalDate expireDate;

    private Integer status;

    private Integer deleted;
}
