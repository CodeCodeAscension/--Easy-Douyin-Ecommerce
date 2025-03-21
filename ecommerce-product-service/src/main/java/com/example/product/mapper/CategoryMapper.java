package com.example.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.common.cache.MybatisRedisCache;
import com.example.product.domain.po.Category;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@CacheNamespace(implementation = MybatisRedisCache.class)
public interface CategoryMapper extends BaseMapper<Category> {
}
