package com.example.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.domain.ResponseResult;
import com.example.product.domain.dto.*;
import com.example.product.domain.vo.ProductInfoVo;
import com.example.product.service.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
//@RequestMapping("/api/v1/products")
@RequestMapping("/products")
@Tag(name = "商品服务接口", description = "商品服务接口")
public class ProductController {

    @Resource
    private IProductService iProductService;

    /**
     * 根据商品id获取商品信息
     * @param productId 商品id
     * @return 商品信息
     */
    @GetMapping("/{productId}")
    @Operation(summary = "根据商品id获取商品信息")
    public ResponseResult<ProductInfoVo> getProductById(@PathVariable Long productId) {
        log.info("根据商品id获取商品信息，productId: {}", productId);
        return iProductService.getProductInfoById(productId);
    }

    /**
     * 指定某种类别查询商品信息
     *
     * @param listProductsDto
     */
    @GetMapping
    @Operation(summary = "指定某种类别查询商品信息")
    public ResponseResult<IPage<ProductInfoVo>> listProducts(@RequestBody ListProductsDto listProductsDto) {
        return iProductService.getProductInfoByCategory(listProductsDto);
    }

    /**
     * 指定条件查询商品信息
     *
     * @param searchProductsDto
     */
    @GetMapping("/search")
    @Operation(summary = "指定条件查询商品信息")
    public ResponseResult<IPage<ProductInfoVo>> searchProducts(@RequestBody SearchProductsDto searchProductsDto) {
        return iProductService.seachProductInfo(searchProductsDto);
    }

    /**
     * 增加库存
     *
     * @param addProductDto
     */
    @PutMapping("/add")
    @Operation(summary = "增加库存")
    public ResponseResult<Object> addProductStock(@RequestBody @Validated AddProductDto addProductDto) {
        return iProductService.addProductStock(addProductDto);
    }

    /**
     * 减少库存
     *
     * @param decProductDto
     */
    @PutMapping("/dec")
    @Operation(summary = "减少库存")
    public ResponseResult<Object> decProductStock(@RequestBody @Validated DecProductDto decProductDto) {
        return iProductService.decProductStock(decProductDto);
    }

    /**
     * 创建商品
     *
     * @param createProductDto
     */
    @PostMapping
    @Operation(summary = "创建商品")
    public ResponseResult<Object> createProduct(@RequestBody @Validated CreateProductDto createProductDto) {
        return iProductService.createProduct(createProductDto);
    }

    /**
     * 更新商品信息
     *
     * @param updateProductDto
     */
    @PutMapping
    @Operation(summary = "更新商品信息")
    public ResponseResult<Object> updateProduct(@RequestBody @Validated UpdateProductDto updateProductDto) {
        return iProductService.updateProduct(updateProductDto);
    }
}
