package com.example.order.mapper;

import com.example.common.cache.MybatisRedisCache;
import com.example.order.domain.po.OrderItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;


/**
 * <p>
 * 订单商品信息数据库 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2025-02-28
 */
@Mapper
@CacheNamespace(implementation = MybatisRedisCache.class)
public interface OrderItemMapper extends BaseMapper<OrderItem> {

}
