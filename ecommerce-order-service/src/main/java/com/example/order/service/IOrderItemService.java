package com.example.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.api.domain.po.CartItem;
import com.example.api.enums.OrderStatusEnum;
import com.example.order.domain.po.OrderItem;

import java.util.List;

public interface IOrderItemService extends IService<OrderItem> {

    /**
     * 根据订单ID更新orderItems数据库表（如果这个商品之前存在，更新原来的表项，否则新建表项）
     * @param orderId 订单ID
     * @param orderItems 订单商品
     */
    void updateOrderItemByOrderId(String orderId, List<CartItem> orderItems);

    /**
     * 根据订单ID获得cartItems列表
     * @param orderId 订单ID
     * @return 结果
     */
    List<CartItem> getCartItemsByOrderId(String orderId);

    /**
     * 设置订单项目状态
     * @param orderId 订单ID
     * @param status 状态
     */
    void setOrderItemsStatus(String orderId, OrderStatusEnum status);
}
