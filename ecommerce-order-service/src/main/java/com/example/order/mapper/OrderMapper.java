package com.example.order.mapper;

import com.example.order.domain.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 订单信息数据库 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2025-02-28
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}
