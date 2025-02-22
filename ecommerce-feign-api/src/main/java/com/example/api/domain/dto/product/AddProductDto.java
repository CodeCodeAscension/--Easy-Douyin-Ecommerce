package com.example.api.domain.dto.product;

import lombok.Data;

@Data
public class AddProductDto {
    private Long productId;
    private Integer addStock;
}
