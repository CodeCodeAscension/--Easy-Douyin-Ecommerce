package com.example.product.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.exception.DatabaseException;
import com.example.product.domain.po.Category;
import com.example.product.mapper.CategoryMapper;
import com.example.product.service.ICategoryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Override
    public List<Long> validateAndGetCategoryIds(List<String> categoryNames) {
        List<Category> categories = this.list(
                Wrappers.<Category>lambdaQuery().in(Category::getCategoryName, categoryNames)
        );

        if (categories.size() != categoryNames.size()) {
            List<String> existNames = categories.stream()
                    .map(Category::getCategoryName)
                    .collect(Collectors.toList());
            List<String> notExist = categoryNames.stream()
                    .filter(name -> !existNames.contains(name))
                    .collect(Collectors.toList());

            // 新增分类
            List<Category> newCategories = notExist.stream()
                    .map(name -> new Category().setCategoryName(name))
                    .collect(Collectors.toList());

            // 增加创建时间和更新时间
            LocalDateTime now = LocalDateTime.now();
            newCategories.forEach(c -> {
                c.setCreateTime(now);
                c.setUpdateTime(now);
            });

            if (!this.saveBatch(newCategories)) {
                throw new DatabaseException("保存分类失败");
            }

            // 重新查询分类
            categories = this.list(
                    Wrappers.<Category>lambdaQuery().in(Category::getCategoryName, categoryNames)
            );
        }

        return categories.stream()
                .map(Category::getId)
                .collect(Collectors.toList());
    }
}
