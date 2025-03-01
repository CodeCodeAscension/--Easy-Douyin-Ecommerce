package com.example.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.example.api.domain.dto.order.PlaceOrderDto;
import com.example.api.domain.dto.order.SearchOrderDto;
import com.example.api.domain.dto.order.UpdateOrderDto;
import com.example.api.domain.po.OrderResult;
import com.example.api.domain.vo.order.OrderInfoVo;
import com.example.api.enums.OrderStatus;
import com.example.order.domain.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单信息数据库 服务类
 * </p>
 *
 * @author author
 * @since 2025-02-28
 */
public interface IOrderService extends IService<Order> {

    // 创建订单
    OrderResult createOrder(PlaceOrderDto placeOrderDto);

    // 更新订单
    Boolean updateOrder(UpdateOrderDto updateOrderDto);

    // 根据订单ID查询订单信息
    OrderInfoVo getOrderById(String orderId);

    //根据订单id修改订单状态为已取消
    Boolean autoCancelOrder(String orderId ,Integer status);

    PageDTO<OrderInfoVo> getAllOrders(Integer pageSize, Integer pageNum);

    PageDTO<OrderInfoVo> searchOrders(Integer pageSize, Integer pageNum, SearchOrderDto seatchOrderDto);
}
