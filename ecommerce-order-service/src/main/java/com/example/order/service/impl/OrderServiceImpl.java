package com.example.order.service.impl;

import com.example.order.domain.Order;
import com.example.order.mapper.OrderMapper;
import com.example.order.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单信息数据库 服务实现类
 * </p>
 *
 * @author author
 * @since 2025-02-28
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

}
