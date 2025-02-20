package com.example.product.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.domain.ResponseResult;
import com.example.product.domain.po.Category;
import com.example.product.domain.po.ProCateRel;
import com.example.product.domain.po.Product;
import com.example.product.domain.vo.ProductInfoVo;
import com.example.product.enums.ProductStatusEnum;
import com.example.product.mapper.productMapper;
import com.example.product.service.ICategoryService;
import com.example.product.service.IProCateRelService;
import com.example.product.service.IProductService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl extends ServiceImpl<productMapper, Product> implements IProductService {

    @Resource
    private productMapper productMapper;

    @Resource
    private ICategoryService iCategoryService;

    @Resource
    private IProCateRelService iProCateRelService;

    /**
     * 根据商品ID查询商品信息
     *
     * @param productId
     */
    @Override
    public ResponseResult<ProductInfoVo> getProductById(Long productId) {
        // 根据商品id查询商品信息
        Product product = productMapper.selectById(productId);
        if (product == null) {
            log.error("商品不存在，productId: {}", productId);
            return ResponseResult.error(ProductStatusEnum.PRODUCT_NOT_EXIST.getErrorCode(), ProductStatusEnum.PRODUCT_NOT_EXIST.getErrorMessage());
        }

        // 根据 productId 从 ProCateRel 表查询商品分类信息
        List<ProCateRel> proCateRels = iProCateRelService.list(Wrappers.<ProCateRel>lambdaQuery().eq(ProCateRel::getProductId, productId));
        if (proCateRels.isEmpty()) {
            log.error("商品分类不存在，productId: {}", productId);
            return ResponseResult.error(ProductStatusEnum.PRODUCT_CATEGORY_NOT_EXIST.getErrorCode(), ProductStatusEnum.PRODUCT_CATEGORY_NOT_EXIST.getErrorMessage());
        }

        // 获取商品分类ID集合
        List<Long> categoryIds = proCateRels.stream().map(ProCateRel::getCategoryId).collect(Collectors.toList());
        // 根据分类ID集合查询分类信息
        List<Category> categories = iCategoryService.listByIds(categoryIds);
        // 查询商品分类信息
        List<String> categoryNames = categories.stream().map(Category::getCategoryName).collect(Collectors.toList());

        // 封装商品信息
        ProductInfoVo productInfoVo = ProductInfoVo.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .sold(product.getSold())
                .stoke(product.getStoke())
                .merchantName(product.getMerchantName())
                .categories(categoryNames)
                .status(product.getStatus())
                .createTime(product.getCreateTime())
                .updateTime(product.getUpdateTime())
                .build();
        return ResponseResult.success(productInfoVo);
    }
}
