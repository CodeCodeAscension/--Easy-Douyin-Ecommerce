package com.example.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.example.api.domain.dto.order.PlaceOrderDto;
import com.example.api.domain.dto.order.SearchOrderDto;
import com.example.api.enums.OrderStatusEnum;
import com.example.order.domain.dto.UpdateOrderDto;
import com.example.api.domain.po.OrderResult;
import com.example.api.domain.vo.order.OrderInfoVo;
import com.example.common.exception.BadRequestException;
import com.example.common.exception.DatabaseException;
import com.example.common.exception.NotFoundException;
import com.example.order.domain.po.Order;
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

    /**
     * 创建订单记录
     * @param userId 用户ID
     * @param placeOrderDto dto
     * @return 创建结果
     * @throws DatabaseException 数据库异常
     * @throws BadRequestException 参数异常
     */
    OrderResult createOrder(Long userId, PlaceOrderDto placeOrderDto) throws DatabaseException, BadRequestException;

    /**
     * 更新订单信息
     * @param updateOrderDto dto
     * @param userId 用户ID
     * @throws DatabaseException 数据库异常
     * @throws BadRequestException 参数异常
     * @throws NotFoundException 未找到异常
     */
    void updateOrder(Long userId, UpdateOrderDto updateOrderDto) throws DatabaseException, BadRequestException, NotFoundException;

    /**
     * 查询订单信息
     * @param orderId 订单号
     * @return 订单信息
     */
    OrderInfoVo getOrderById(String orderId);

    /**
     * 取消订单
     * @param userId 用户ID
     * @param orderId 订单ID
     * @throws BadRequestException 参数异常
     * @throws NotFoundException 未找到异常
     */
    void cancelOrder(Long userId, String orderId) throws BadRequestException, NotFoundException;

    /**
     * 修改订单状态
     * @param orderId 订单ID
     * @param status 状态值
     */
    void changeOrderStatus(String orderId , OrderStatusEnum status);

    /**
     * 获取订单状态
     * @param orderId 订单ID
     * @return 订单状态
     */
    OrderStatusEnum getOrderStatus(String orderId);

    /**
     * 获得某个用户的所有订单
     * @param userId 用户ID
     * @param pageSize 页大小
     * @param pageNum 页号
     * @return 订单信息
     */
    PageDTO<OrderInfoVo> getAllOrders(Long userId, Integer pageSize, Integer pageNum);

    /**
     * 查询某个用户订单信息
     * @param userId 用户ID
     * @param pageSize 页大小
     * @param pageNum 页号
     * @param seatchOrderDto 查询dto
     * @return 订单信息
     */
    PageDTO<OrderInfoVo> searchOrders(Long userId, Integer pageSize, Integer pageNum, SearchOrderDto seatchOrderDto);
}
