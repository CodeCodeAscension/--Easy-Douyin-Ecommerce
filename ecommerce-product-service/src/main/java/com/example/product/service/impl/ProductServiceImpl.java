package com.example.product.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.domain.ResponseResult;
import com.example.product.domain.dto.ListProductsDto;
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

    /**
     * 指定某种类别查询商品信息
     *
     * @param listProductsDto
     */
    @Override
    public ResponseResult<IPage<ProductInfoVo>> listProducts(ListProductsDto listProductsDto) {
        if (listProductsDto == null) {
            log.error("参数不能为空");
            return ResponseResult.error(ProductStatusEnum.PARAM_NOT_NULL.getErrorCode(), ProductStatusEnum.PARAM_NOT_NULL.getErrorMessage());
        }
        // 当前页面
        int current = listProductsDto.getPage();
        // 每页显示条数
        int size = listProductsDto.getPageSize();
        // 类型名称
        String categoryName = listProductsDto.getCategoryName();

        // 分类名称若为null则查询所有商品
        if (categoryName == null) {

        }else {
            // 不为null则根据分类名称查询分类信息
            Category category = iCategoryService.getOne(Wrappers.<Category>lambdaQuery().eq(Category::getCategoryName, categoryName));
            if (category == null) {
                log.error("商品分类不存在，categoryName: {}", categoryName);
                return ResponseResult.error(ProductStatusEnum.PRODUCT_CATEGORY_NOT_EXIST.getErrorCode(), ProductStatusEnum.PRODUCT_CATEGORY_NOT_EXIST.getErrorMessage());
            }
            // 根据分类ID查询商品ID
            List<ProCateRel> proCateRels = iProCateRelService.list(Wrappers.<ProCateRel>lambdaQuery().eq(ProCateRel::getCategoryId, category.getId()));
            if (proCateRels.isEmpty()) {
                log.error("该分类下的商品不存在，categoryName: {}", categoryName);
                return ResponseResult.error(ProductStatusEnum.CATEGORY_PRODUCT_NOT_EXIST.getErrorCode(), ProductStatusEnum.CATEGORY_PRODUCT_NOT_EXIST.getErrorMessage());
            }
            // TODO
        }
        return null;
    }

}
