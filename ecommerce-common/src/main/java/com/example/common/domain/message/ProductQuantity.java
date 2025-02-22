package com.example.common.domain.message;

import lombok.Data;

@Data
public class ProductQuantity {
    private Long productId;
    private Integer quantity;
}
