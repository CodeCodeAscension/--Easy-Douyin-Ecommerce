package com.example.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.domain.ResponseResult;
import com.example.product.domain.dto.AddProductDto;
import com.example.product.domain.dto.DecProductDto;
import com.example.product.domain.dto.ListProductsDto;
import com.example.product.domain.dto.SearchProductsDto;
import com.example.product.domain.vo.ProductInfoVo;
import com.example.product.service.IProductService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
        return iProductService.getProductInfoById(productId);
    }

    /**
     * 指定某种类别查询商品信息
     *
     * @param listProductsDto
     */
    @GetMapping
    public ResponseResult<IPage<ProductInfoVo>> listProducts(@RequestBody ListProductsDto listProductsDto) {
        return iProductService.getProductInfoByCategory(listProductsDto);
    }

    /**
     * 指定条件查询商品信息
     *
     * @param searchProductsDto
     */
    @GetMapping("/search")
    public ResponseResult<IPage<ProductInfoVo>> searchProducts(@RequestBody SearchProductsDto searchProductsDto) {
        return iProductService.seachProductInfo(searchProductsDto);
    }

    /**
     * 增加库存
     *
     * @param addProductDto
     */
    @PutMapping("/add")
    public ResponseResult<Object> addProductStock(@RequestBody AddProductDto addProductDto) {
        return iProductService.addProductStock(addProductDto);
    }

    /**
     * 减少库存
     *
     * @param decProductDto
     */
    @PutMapping("/dec")
    public ResponseResult<Object> decProductStock(@RequestBody DecProductDto decProductDto) {
        return iProductService.decProductStock(decProductDto);
    }
}
