package com.example.order.service.impl;

import com.example.order.domain.OrderItem;
import com.example.order.mapper.OrderItemMapper;
import com.example.order.service.IOrderItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单商品信息数据库 服务实现类
 * </p>
 *
 * @author author
 * @since 2025-02-28
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements IOrderItemService {

}
