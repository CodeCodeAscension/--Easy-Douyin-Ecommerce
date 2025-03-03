package com.example.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.product.domain.po.Category;

import java.util.List;

/**
 * 商品分类
 * @since 2025-02-20
 * @author darkmatter
 */
public interface ICategoryService extends IService<Category> {

    /**
     * 创建或者获取类别ID
     * @param categoryNames 类别名称列表
     * @return 类别ID列表
     */
    List<Long> validateAndGetCategoryIds(List<String> categoryNames);
}
