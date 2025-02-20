package com.example.product.controller;

import com.example.common.domain.ResponseResult;
import com.example.product.domain.vo.ProductInfoVo;
import com.example.product.service.IProductService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
//@RequestMapping("/api/v1/products")
@RequestMapping("/products")
public class ProductController {

    @Resource
    private IProductService iProductService;

    /**
     * 根据商品id获取商品信息
     * @param productId 商品id
     * @return 商品信息
     */
    @GetMapping("/{productId}")
    public ResponseResult<ProductInfoVo> getProductById(@PathVariable Long productId) {
        log.info("根据商品id获取商品信息，productId: {}", productId);
        return iProductService.getProductById(productId);
    }
}
