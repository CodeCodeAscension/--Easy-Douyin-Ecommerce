package com.example.product.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListProductsDto {

    private Integer page;

    private Integer pageSize;

    private String categoryName;
}
