package com.example.payment.domain.dto;

import com.example.payment.enums.CreditStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "银行卡信息更新DTO")
public class CreditUpdateDto {
    @NotBlank(message = "卡号不能为空")
    @Schema(description = "银行卡号")
    private String cardNumber;
    @Schema(description = "卡验证值")
    private Integer cvv;

    @NotNull(message = "请指定用户ID")
    @Schema(description = "用户ID")
    private Long userId;


    @DecimalMin(value = "0.0", message = "余额不能小于0")
    @Schema(description = "余额")
    private Float balance;

    @Future(message = "过期日期必须是未来的日期")
    @Schema(description = "过期时间")
    private LocalDate expireDate;

    @Schema(description = "卡状态")
    private CreditStatusEnum status;
}
