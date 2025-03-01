package com.example.cart.mapper;

import com.example.cart.domain.po.Cart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.common.cache.MybatisRedisCache;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;


/**
 * <p>
 * 购物车信息数据库 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2025-02-14
 */
@Mapper
@CacheNamespace(implementation = MybatisRedisCache.class)
public interface CartMapper extends BaseMapper<Cart> {

}
