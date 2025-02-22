package com.example.payment.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class CreditVo {
    private String cardNumber;
    private Integer cvv;
    private Long userId;
    private Float balance;
    private LocalDate expireDate;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
