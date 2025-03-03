package com.example.checkout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.checkout.domain.po.CheckoutPo;
import com.example.common.cache.MybatisRedisCache;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@CacheNamespace(implementation = MybatisRedisCache.class)
public interface CheckoutMapper extends BaseMapper<CheckoutPo> {
}
