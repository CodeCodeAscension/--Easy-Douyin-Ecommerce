package com.example.cart.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartInfoVo {


    private Long id;
    private Long userId;
    private Integer status;
    private List<CartItemInfo> cartItems;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}