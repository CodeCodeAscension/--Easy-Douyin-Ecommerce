package com.example.order.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.example.api.client.ProductClient;
import com.example.api.domain.dto.order.PlaceOrderDto;

import com.example.api.domain.dto.order.SearchOrderDto;
import com.example.api.domain.dto.order.UpdateOrderDto;
import com.example.api.domain.po.CartItem;
import com.example.api.domain.po.OrderResult;
import com.example.api.domain.vo.order.AddressInfoVo;
import com.example.api.domain.vo.order.OrderInfoVo;
import com.example.api.domain.vo.product.ProductInfoVo;
import com.example.api.enums.OrderStatus;
import com.example.common.config.rabbitmq.RetryableCorrelationData;
import com.example.common.exception.BadRequestException;
import com.example.common.exception.DatabaseException;
import com.example.common.exception.NotFoundException;
import com.example.common.util.UserContextUtil;
import com.example.order.config.RabbitMQDLXConfig;
import com.example.order.domain.po.Address;
import com.example.order.domain.po.Order;
import com.example.order.mapper.OrderMapper;
import com.example.order.service.IAddressService;
import com.example.order.service.IOrderItemService;
import com.example.order.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单信息数据库 服务实现类
 * </p>
 *
 * @author author
 * @since 2025-02-28
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    private final RabbitTemplate rabbitTemplate;
    private final IAddressService iAddressService;
    private final IOrderItemService orderItemService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderResult createOrder(Long userId, PlaceOrderDto placeOrderDto) {
        if(placeOrderDto.getCartItems() == null || placeOrderDto.getCartItems().isEmpty()) {
            throw new BadRequestException("至少要有订单商品");
        }
        //dto转po
        Order order = new Order();
        //拷贝属性
        order.setUserId(userId);
        order.setUserCurrency(placeOrderDto.getUserCurrency());
        order.setAddressId(placeOrderDto.getAddressId());
        order.setEmail(placeOrderDto.getEmail());
        order.setStatus(OrderStatus.WAIT_FOR_PAY);
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        order.setDeleted(0);

        //保存订单
        boolean save = save(order);
        //返回结果
        OrderResult orderResult = new OrderResult();
        if (save) {
            //保存OrderItem信息
            orderItemService.updateOrderItemByOrderId(order.getOrderId(), placeOrderDto.getCartItems());
            orderResult.setOrderId(order.getOrderId());
            //设置订单ttl半个小时
            RetryableCorrelationData data = new RetryableCorrelationData(
                    RabbitMQDLXConfig.ORDER_EXCHANGE,
                    RabbitMQDLXConfig.ORDER_ROUTING_KEY,
                    orderResult.getOrderId(),
                    message -> {
                        message.getMessageProperties().setExpiration(String.valueOf(30 * 60 * 1000L));
                        return message;
                    }
            );
            rabbitTemplate.convertAndSend(data.getExchange(), data.getRoutingKey(), data.getMessage(), data.getMessagePostProcessor(), data);
            return orderResult;
        } else {
            throw new DatabaseException("数据库异常");
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrder(@NotNull UpdateOrderDto updateOrderDto) {
        //封装po
        Order order = this.getById(updateOrderDto.getOrderId());
        if(order == null) {
            throw new NotFoundException("没有该订单");
        } else if(order.getStatus() != OrderStatus.WAIT_FOR_PAY) {
            throw new BadRequestException("此时订单不可以修改");
        }
        order.setOrderId(updateOrderDto.getOrderId());
        order.setUserCurrency(updateOrderDto.getUserCurrency());
        order.setAddressId(updateOrderDto.getAddressId());
        order.setEmail(updateOrderDto.getEmail());
        order.setUpdateTime(LocalDateTime.now());

        if(!this.updateById(order)) {
            throw new DatabaseException("数据库异常");
        }
        //更新OrderItem信息
        orderItemService.updateOrderItemByOrderId(order.getOrderId(), updateOrderDto.getCartItems());
    }

    @Override
    public OrderInfoVo getOrderById(String orderId) {
        Order order = getOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderId, orderId));
        return getOrderInfoVo(order);
    }

    @Override
    public void cancelOrder(Long userId, String orderId) throws BadRequestException, NotFoundException {
        Order order = getOne(Wrappers.<Order>lambdaQuery()
                .eq(Order::getOrderId, orderId)
                .eq(Order::getUserId, userId)
        );
        if(order == null) {
            throw new NotFoundException("该订单不存在");
        }
        if(order.getStatus() != OrderStatus.WAIT_FOR_PAY) {
            throw new BadRequestException("该订单不可以取消");
        }
        order.setStatus(OrderStatus.CANCELED);
        order.setUpdateTime(LocalDateTime.now());
        updateById(order);
    }

    //自动取消订单
    @Override
    public Boolean autoCancelOrder(String orderId, Integer status) {
        Order order = getOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderId, orderId));
        if (order != null) {
            order.setStatus(OrderStatus.fromCode(status));
            order.setUpdateTime(LocalDateTime.now());
            return updateById(order);
        }
        return false;
    }


    // 分页查询订单信息
    @Override
    public PageDTO<OrderInfoVo> getAllOrders(Long userId, Integer pageSize, Integer pageNum) {
        // 分页查询订单信息
        Page<Order> page = this.lambdaQuery()
                .eq(Order::getUserId, userId)
                .page(new Page<>(pageNum, pageSize));

        // 获取分页记录
        List<Order> records = page.getRecords();

        // 如果没有记录，返回空的 PageDTO
        if (records.isEmpty()) {
            return new PageDTO<>();
        }

        // 转换 Order 列表为 OrderInfoVo 列表
        List<OrderInfoVo> orderInfoVos = records.stream()
                .map(this::getOrderInfoVo)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 创建 PageDTO 对象
        PageDTO<OrderInfoVo> pageDTO = new PageDTO<>();
        pageDTO.setCurrent(pageNum);
        pageDTO.setSize(pageSize);
        pageDTO.setTotal(page.getTotal());
        pageDTO.setRecords(orderInfoVos);

        return pageDTO;
    }


    // 条件分页查询订单信息
    @Override
    public PageDTO<OrderInfoVo> searchOrders(Long userId, Integer pageSize, Integer pageNum, @NotNull SearchOrderDto seatchOrderDto) {
        // 分页查询订单信息
        Page<Order> page = this.lambdaQuery()
                .eq(Order::getUserId, userId)
                .eq(seatchOrderDto.getStatus() != null, Order::getStatus, seatchOrderDto.getStatus())
                .ge(seatchOrderDto.getCreateDateLowerBound() != null, Order::getCreateTime, seatchOrderDto.getCreateDateLowerBound())
                .le(seatchOrderDto.getCreateDateUpperBound() != null, Order::getCreateTime, seatchOrderDto.getCreateDateUpperBound())
                .ge(seatchOrderDto.getPaymentDateLowerBound()!= null, Order::getPayTime, seatchOrderDto.getPaymentDateLowerBound())
                .le(seatchOrderDto.getPaymentDateUpperBound() != null, Order::getPayTime, seatchOrderDto.getPaymentDateUpperBound())
                .page(new Page<>(pageNum, pageSize));

        // 获取分页记录
        List<Order> records = page.getRecords();

        // 如果没有记录，返回空的 PageDTO
        if (records.isEmpty()) {
            return new PageDTO<>();
        }

        // 转换 Order 列表为 OrderInfoVo 列表
        List<OrderInfoVo> orderInfoVos = records.stream()
                .map(this::getOrderInfoVo)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 创建 PageDTO 对象
        PageDTO<OrderInfoVo> pageDTO = new PageDTO<>();
        pageDTO.setCurrent(pageNum);
        pageDTO.setSize(pageSize);
        pageDTO.setTotal(page.getTotal());
        pageDTO.setRecords(orderInfoVos);

        return pageDTO;
    }

    @Nullable
    private OrderInfoVo getOrderInfoVo(Order order) {
        if (order != null) {
            //封装vo
            OrderInfoVo orderInfoVo = new OrderInfoVo();
            BeanUtils.copyProperties(order, orderInfoVo);

            //查询地址信息
            Address byId = iAddressService.getById(order.getAddressId());
            AddressInfoVo addressInfoVo = new AddressInfoVo();
            BeanUtils.copyProperties(byId, addressInfoVo);
            orderInfoVo.setAddress(addressInfoVo);
            //封装CartItems
            orderInfoVo.setCartItems(orderItemService.getCartItemsByOrderId(order.getOrderId()));
            return orderInfoVo;
        }
        return null;
    }

}
