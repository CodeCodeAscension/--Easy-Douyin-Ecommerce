package com.example.common.domain.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductQuantity {
    private Long productId;
    private Integer quantity;


}
