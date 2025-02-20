package com.example.product.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchProductsDto {

        private Integer page;

        private Integer pageSize;

        private String productName;

        private Float priceLow;

        private Float priceHigh;

        private Integer sold;

        private Integer stoke;

        private String merchantName;

        private String categoryName;

}
