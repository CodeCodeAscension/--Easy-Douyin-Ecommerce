package com.example.common.domain.message;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class ProductQuantity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long productId;
    private Integer quantity;
}
