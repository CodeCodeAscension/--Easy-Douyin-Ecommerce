package com.example.product.convert;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.product.domain.po.Category;
import com.example.product.domain.po.ProCateRel;
import com.example.product.domain.po.Product;
import com.example.product.domain.vo.ProductInfoVo;
import com.example.product.service.ICategoryService;
import com.example.product.service.IProCateRelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductInfoVoConvert {

    private final IProCateRelService iProCateRelService;
    private final ICategoryService iCategoryService;

    /**
     * 将 Product 对象转换为 ProductInfoVo 对象
     *
     * @param product 商品实体
     * @return ProductInfoVo
     */
    public ProductInfoVo convertToProductInfoVo(Product product) {
        // 根据 productId 查询商品分类关系
        List<ProCateRel> proCateRels = iProCateRelService.list(
                Wrappers.<ProCateRel>lambdaQuery().eq(ProCateRel::getProductId, product.getId())
        );

        // 获取分类ID集合
        List<Long> categoryIds = proCateRels.stream()
                .map(ProCateRel::getCategoryId)
                .collect(Collectors.toList());

        // 根据分类ID查询分类信息
        List<Category> categories = iCategoryService.listByIds(categoryIds);

        // 获取分类名称集合
        List<String> categoryNames = categories.stream()
                .map(Category::getCategoryName)
                .collect(Collectors.toList());

        // 封装商品信息
        return ProductInfoVo.builder()
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
    }
}
