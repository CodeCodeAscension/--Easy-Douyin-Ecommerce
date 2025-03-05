package com.example.payment.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "添加银行卡DTO")
public class CreditCreateDto {
    @NotBlank(message = "卡号不能为空")
    @Schema(description = "银行卡号")
    private String cardNumber;

    @NotNull(message = "CVV不能为空")
    @Schema(description = "银行卡CVV")
    private Integer cvv;
}
