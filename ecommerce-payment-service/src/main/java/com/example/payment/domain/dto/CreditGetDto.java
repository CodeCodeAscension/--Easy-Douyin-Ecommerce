package com.example.payment.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "获取银行卡信息DTO")
@NoArgsConstructor
public class CreditGetDto {
    @NotBlank(message = "银行卡号不为空")
    @Schema(description = "银行卡号")
    private String cardNumber;
}
