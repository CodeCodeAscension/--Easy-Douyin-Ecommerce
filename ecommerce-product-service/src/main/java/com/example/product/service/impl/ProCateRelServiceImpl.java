package com.example.product.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.exception.DatabaseException;
import com.example.product.domain.po.Category;
import com.example.product.domain.po.ProCateRel;
import com.example.product.mapper.ProCateRelMapper;
import com.example.product.service.ICategoryService;
import com.example.product.service.IProCateRelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProCateRelServiceImpl extends ServiceImpl<ProCateRelMapper, ProCateRel> implements IProCateRelService {

    private final ICategoryService iCategoryService;

    @Override
    public void saveProductCategories(Long productId, List<Long> categoryIds) {
        List<ProCateRel> relations = categoryIds.stream()
                .map(cid -> new ProCateRel(productId, cid))
                .collect(Collectors.toList());
        // 保存创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        relations.forEach(r -> {
            r.setCreateTime(now);
            r.setUpdateTime(now);
        });
        if (!this.saveBatch(relations)) {
            throw new DatabaseException("保存分类关系失败");
        }
    }

    @Override
    public List<String> getProductCategoryNames(Long productId) {
        // 根据 productId 查询商品分类关系
        List<ProCateRel> proCateRels = this.list(
                Wrappers.<ProCateRel>lambdaQuery().eq(ProCateRel::getProductId, productId)
        );

        // 获取分类ID集合
        List<Long> categoryIds = proCateRels.stream()
                .map(ProCateRel::getCategoryId)
                .collect(Collectors.toList());

        // 根据分类ID查询分类信息
        List<Category> categories = iCategoryService.listByIds(categoryIds);

        // 获取分类名称集合
        return categories.stream()
                .map(Category::getCategoryName)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateProductCategories(Long productId, Set<Long> newCategoryIds) {
        Set<Long> oldCategoryIds = this.list(Wrappers.<ProCateRel>lambdaQuery()
                .eq(ProCateRel::getProductId, productId))
                .stream().map(ProCateRel::getCategoryId)
                .collect(Collectors.toSet());
        // 求差集，删去现在不存在的关系，保留新存在的关系
        List<Long> delIds = oldCategoryIds.stream()
                .filter(id -> !newCategoryIds.contains(id))
                .toList();
        this.remove(Wrappers.<ProCateRel>lambdaQuery()
                    .eq(ProCateRel::getProductId, productId)
                    .in(ProCateRel::getCategoryId, delIds));
        List<Long> addIds = newCategoryIds.stream()
                .filter(id -> !oldCategoryIds.contains(id))
                .toList();
        this.saveProductCategories(productId, addIds);
    }
}
