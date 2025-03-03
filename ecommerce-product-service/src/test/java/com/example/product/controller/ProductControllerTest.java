package com.example.product.controller;

import com.example.common.domain.ResponseResult;
import com.example.api.domain.vo.product.ProductInfoVo;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductControllerTest {

    @Resource
    private ProductController productController;

    @Test
    void getProductById() {
        ResponseResult<ProductInfoVo> productById = productController.getProductById(1L);
        assertNotNull(productById);
        ProductInfoVo data = productById.getData();
        System.out.println(data);
    }
}