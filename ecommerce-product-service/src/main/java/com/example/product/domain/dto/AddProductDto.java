package com.example.product.domain.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class AddProductDto {

    @NonNull
    private Long productId;

    @NonNull
    private Integer addStock;
}
