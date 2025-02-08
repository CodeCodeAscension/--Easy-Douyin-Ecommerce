package com.example.api.client;

import com.example.api.domain.dto.product.GetProductDto;
import com.example.api.domain.dto.product.ListProductsDto;
import com.example.api.domain.dto.product.SearchProductsDto;
import com.example.api.domain.vo.product.GetProductVo;
import com.example.api.domain.vo.product.ListProductsVo;
import com.example.api.domain.vo.product.SearchProductsVo;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("product-service")
public interface ProductClient {
    ListProductsVo listProducts(ListProductsDto listProductsDto);
    GetProductVo getProduct(GetProductDto getProductDto);
    SearchProductsVo searchProducts(SearchProductsDto searchProductsDto);
}
