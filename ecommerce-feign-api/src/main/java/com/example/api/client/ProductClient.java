package com.example.api.client;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.api.client.fallback.ProductClientFallBack;
import com.example.api.domain.dto.product.AddProductDto;
import com.example.api.domain.dto.product.DecProductDto;
import com.example.api.domain.dto.product.ListProductsDto;
import com.example.api.domain.dto.product.SearchProductsDto;
import com.example.api.domain.vo.product.ProductInfoVo;
import com.example.common.domain.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "product-service", fallbackFactory = ProductClientFallBack.class)
public interface ProductClient {
    // 通过ID获取商品信息
    @GetMapping("/api/v1/products/{productId}")
    ResponseResult<ProductInfoVo> getProductInfoById(@PathVariable("productId") Long productId);

    // 通过类别获取商品信息
    @GetMapping("/api/v1/products")
    ResponseResult<IPage<ProductInfoVo>> getProductInfoByCategory(@RequestBody ListProductsDto listProductsDto);

    // 查询商品信息
    @GetMapping("/api/v1/products/search")
    ResponseResult<IPage<ProductInfoVo>> seachProductInfo(@RequestBody SearchProductsDto searchProductsDto);

    // 增加存货
    @PutMapping("/api/v1/products/add")
    ResponseResult<Object> addProductStock(@RequestBody AddProductDto addProductDto);

    // 减少存货
    @PutMapping("/api/v1/inner/products/dec")
    ResponseResult<Object> decProductStock(@RequestBody DecProductDto decProductDto);
}
