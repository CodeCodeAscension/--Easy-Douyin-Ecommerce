package com.example.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.product.domain.po.ProCateRel;

import java.util.List;
import java.util.Set;

public interface IProCateRelService extends IService<ProCateRel> {

    /**
     * 保存商品ID与分类ID的关系
     * @param productId 商品ID
     * @param categoryIds 分类ID列表
     */
    void saveProductCategories(Long productId, List<Long> categoryIds);

    /**
     * 根据商品ID获取所属的分类名称
     * @param productId 商品ID
     * @return 分类名称列表
     */
    List<String> getProductCategoryNames(Long productId);

    /**
     * 更新商品与分类的关系
     * @param productId 商品ID
     * @param newCategoryIds 新的分类ID
     */
    void updateProductCategories(Long productId, Set<Long> newCategoryIds);
}
