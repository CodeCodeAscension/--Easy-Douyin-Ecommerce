package com.example.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.product.domain.dto.*;
import com.example.api.domain.dto.product.*;
import com.example.common.domain.ResponseResult;
import com.example.common.util.UserContextUtil;
import com.example.api.domain.vo.product.ProductInfoVo;
import com.example.product.service.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@Tag(name = "商品服务接口", description = "商品服务接口")
public class ProductController {

    @Resource
    private IProductService iProductService;

    @GetMapping("/{productId}")
    @Operation(summary = "根据商品id获取商品信息")
    public ResponseResult<ProductInfoVo> getProductById(@PathVariable Long productId) {
        return ResponseResult.success(iProductService.getProductInfoById(productId));
    }

    @GetMapping
    @Operation(summary = "指定某种类别查询商品信息")
    public ResponseResult<IPage<ProductInfoVo>> listProducts(@RequestBody @Validated ListProductsDto listProductsDto) {
        return ResponseResult.success(iProductService.getProductInfoByCategory(listProductsDto));
    }

    @GetMapping("/search")
    @Operation(summary = "指定条件查询商品信息")
    public ResponseResult<IPage<ProductInfoVo>> searchProducts(@RequestBody @Validated SearchProductsDto searchProductsDto) {
        return ResponseResult.success(iProductService.searchProductInfo(searchProductsDto));
    }

    @PutMapping("/add")
    @Operation(summary = "增加库存")
    public ResponseResult<Object> addProductStock(@RequestBody @Validated AddProductDto addProductDto) {
        UserContextUtil.getUserId();
        iProductService.addProductStock(addProductDto);
        return ResponseResult.success();
    }

    @PostMapping
    @Operation(summary = "创建商品")
    public ResponseResult<Object> createProduct(@RequestBody @Validated CreateProductDto createProductDto) {
        UserContextUtil.getUserId();
        iProductService.createProduct(createProductDto);
        return ResponseResult.success();
    }

    @PostMapping("/batch")
    @Operation(summary = "批量创建商品")
    public ResponseResult<Object> createProducts(@RequestBody @Validated List<CreateProductDto> dtos) {
        UserContextUtil.getUserId();
        iProductService.createProducts(dtos);
        return ResponseResult.success();
    }

    @PutMapping
    @Operation(summary = "更新商品信息")
    public ResponseResult<Object> updateProduct(@RequestBody @Validated UpdateProductDto updateProductDto) {
        UserContextUtil.getUserId();
        iProductService.updateProduct(updateProductDto);
        return ResponseResult.success();
    }
}
