package com.example.payment.domain.dto;

import com.example.payment.enums.CreditStatusEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditUpdateDto {
    @NotBlank(message = "卡号不能为空")
    private String cardNumber;

    private Integer cvv;

    @NotNull(message = "请指定用户ID")
    private Long userId;


    @DecimalMin(value = "0.0", message = "余额不能小于0")
    private Float balance;

    @Future(message = "过期日期必须是未来的日期")
    private LocalDate expireDate;

    private CreditStatusEnum status;
}
