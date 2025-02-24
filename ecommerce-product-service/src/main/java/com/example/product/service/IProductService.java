package com.example.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.domain.ResponseResult;
import com.example.product.domain.dto.AddProductDto;
import com.example.product.domain.dto.DecProductDto;
import com.example.product.domain.dto.ListProductsDto;
import com.example.product.domain.dto.SearchProductsDto;
import com.example.product.domain.po.Product;
import com.example.product.domain.vo.ProductInfoVo;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 商品服务类
 * @since 2025-02-20
 * @author darkmatter
 */
public interface IProductService extends IService<Product> {

    /**
     * 创建商品
     */

    /**
     * 根据商品ID查询商品信息
     */
    ResponseResult<ProductInfoVo> getProductInfoById(Long productId);

    /**
     * 指定某种类别查询商品信息
     */
    ResponseResult<IPage<ProductInfoVo>> getProductInfoByCategory(ListProductsDto listProductsDto);

    /**
     * 指定条件查询商品信息
     */
    ResponseResult<IPage<ProductInfoVo>> seachProductInfo(SearchProductsDto searchProductsDto);

    /**
     * 增加库存
     */
    ResponseResult<Object> addProductStock(AddProductDto addProductDto);

    /**
     * 减少库存
     */
    ResponseResult<Object> decProductStock(DecProductDto decProductDto);
}
