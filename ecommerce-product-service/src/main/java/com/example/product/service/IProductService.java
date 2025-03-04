package com.example.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.exception.SystemException;
import com.example.common.exception.UserException;
import com.example.api.domain.dto.product.*;
import com.example.product.domain.dto.*;
import com.example.product.domain.po.Product;
import com.example.api.domain.vo.product.ProductInfoVo;

import java.util.List;

/**
 * 商品服务类
 * @since 2025-02-20
 * @author darkmatter
 */
public interface IProductService extends IService<Product> {

    /**
     * 创建商品
     * @param createProductDto dto
     * @throws SystemException 系统异常
     */
    void createProduct(CreateProductDto createProductDto) throws SystemException;

    /**
     * 批量创建商品
     * @param createProductDtos dtos
     * @throws SystemException 系统异常
     */
    void createProducts(List<CreateProductDto> createProductDtos) throws SystemException;

    /**
     * 根据商品ID查询商品信息
     * @param productId 商品ID
     * @throws UserException 用户异常
     * @throws SystemException 系统异常
     * @return 商品信息vo
     */
    ProductInfoVo getProductInfoById(Long productId) throws UserException, SystemException;

    /**
     * 指定某种类别查询商品信息
     * @param listProductsDto dto
     * @return 查询结果
     * @throws UserException 用户异常
     * @throws SystemException 系统异常
     */
    IPage<ProductInfoVo> getProductInfoByCategory(ListProductsDto listProductsDto) throws UserException, SystemException;

    /**
     * 指定条件查询商品信息
     * @param searchProductsDto dto
     * @return 查询结果
     * @throws UserException 用户异常
     * @throws SystemException 系统异常
     */
    IPage<ProductInfoVo> searchProductInfo(SearchProductsDto searchProductsDto);

    /**
     * 增加库存
     * @param addProductDto dto
     * @throws UserException 用户异常
     * @throws SystemException 系统异常
     */
    void addProductStock(AddProductDto addProductDto) throws UserException, SystemException;

    /**
     * 减少库存
     * @param decProductDto dto
     * @throws UserException 用户异常
     * @throws SystemException 系统异常
     */
    void decProductStock(DecProductDto decProductDto) throws UserException, SystemException;

    /**
     * 增加销量
     * @param addProductSoldDto dto
     * @throws UserException 用户异常
     * @throws SystemException 系统异常
     */
    void addProductSales(AddProductSoldDto addProductSoldDto) throws UserException, SystemException;

    /**
     * 更新商品信息
     * @param updateProductDto dto
     * @throws UserException 用户异常
     * @throws SystemException 系统异常
     */
    void updateProduct(UpdateProductDto updateProductDto) throws UserException, SystemException;
}
