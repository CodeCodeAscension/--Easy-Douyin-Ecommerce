package com.example.payment.domain.vo;

import com.example.payment.enums.CreditStatusEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "银行卡信息")
public class CreditVo {
    @Schema(description = "银行卡号")
    private String cardNumber;
    @Schema(description = "卡CVV")
    private Integer cardCvv;
    @Schema(description = "所属用户ID")
    private Long userId;
    @Schema(description = "余额")
    private Float balance;
    @Schema(description = "过期时间")
    private LocalDate expireDate;
    @Schema(description = "银行卡状态")
    private CreditStatusEnum status;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
