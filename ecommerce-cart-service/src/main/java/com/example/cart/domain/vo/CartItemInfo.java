package com.example.cart.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartItemInfo {
    private Long id;
    private Long productId;
    private Integer quantity;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
