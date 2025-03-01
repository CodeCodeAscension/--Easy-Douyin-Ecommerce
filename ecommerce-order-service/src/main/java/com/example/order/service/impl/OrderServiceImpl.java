package com.example.order.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.example.api.client.ProductClient;
import com.example.api.domain.dto.order.PlaceOrderDto;

import com.example.api.domain.dto.order.SearchOrderDto;
import com.example.api.domain.dto.order.UpdateOrderDto;
import com.example.api.domain.po.Address;
import com.example.api.domain.po.CartItem;
import com.example.api.domain.po.OrderResult;
import com.example.api.domain.vo.order.OrderInfoVo;
import com.example.api.domain.vo.product.ProductInfoVo;
import com.example.api.enums.OrderStatus;
import com.example.common.util.UserContextUtil;
import com.example.order.config.RabbitMQDLXConfig;
import com.example.order.domain.Order;
import com.example.order.mapper.OrderMapper;
import com.example.order.service.IAddressService;
import com.example.order.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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


    private final RabbitMQDLXConfig rabbitMQDLXConfig;
    private final RabbitTemplate rabbitTemplate;
    private final IAddressService iAddressService;
    private final ProductClient productClient;
    //创建订单
    @Override
    public OrderResult createOrder(@NotNull PlaceOrderDto placeOrderDto) {
        //获取用户登录信息
        Long userId = UserContextUtil.getUserId();

        //dto转po
        Order order = new Order();

        //拷贝属性
        //获取当前时间
        LocalDateTime now = LocalDateTime.now();
        order.setUserId(userId);
        order.setUserCurrency(placeOrderDto.getUserCurrency());
        order.setAddressId(placeOrderDto.getAddress().getId());
        order.setEmail(placeOrderDto.getEmail());
        order.setOrderItems(placeOrderDto.getCartItems().toString());
        order.setStatus(OrderStatus.WAIT_FOR_PAY);
        order.setCreateTime(now);
        order.setUpdateTime(now);
        order.setDeleted(0);

        //保存订单
        boolean save = save(order);

        //返回结果
        OrderResult orderResult = new OrderResult();
        if (save) {
            orderResult.setOrderId(order.getOrderId());

            //设置订单ttl半个小时
            rabbitTemplate.convertAndSend(rabbitMQDLXConfig.getORDER_EXCHANGE(), rabbitMQDLXConfig.getORDER_ROUTING_KEY(), orderResult.getOrderId(), message -> {
                message.getMessageProperties().setExpiration("1800000");
                return message;
            });
        }
        return orderResult;

    }


    //修改订单信息
    @Transactional
    @Override
    public Boolean updateOrder(@NotNull UpdateOrderDto updateOrderDto) {

        //如果订单已支付或者取消不能修改
        if (updateOrderDto.getStatus() .equals( OrderStatus.PAID.getCode())
                || updateOrderDto.getStatus() .equals(OrderStatus.CANCELED.getCode())
                ||updateOrderDto.getStatus().equals(OrderStatus.WAIT_FOR_PAY.getCode())) {
            return false;
        }

        //封装po
        Order order = new Order();
        order.setOrderId(updateOrderDto.getOrderId());
        order.setStatus(OrderStatus.fromCode(updateOrderDto.getStatus()));
        order.setUserCurrency(updateOrderDto.getUserCurrency());
        order.setAddressId(updateOrderDto.getAddress().getId());
        order.setEmail(updateOrderDto.getEmail());
        order.setOrderItems(updateOrderDto.getCartItems().toString());
        order.setUpdateTime(LocalDateTime.now());


        return updateById(order);


    }

    //根据订单id查询订单信息
    @Override
    public OrderInfoVo getOrderById(String orderId) {
        Order order = getOne(lambdaQuery().eq(Order::getOrderId, orderId));
        OrderInfoVo orderInfoVo = getOrderInfoVo(order);
        if (orderInfoVo != null) return orderInfoVo;
        return null;
    }

    //自动取消订单
    @Override
    public Boolean autoCancelOrder(String orderId,Integer status) {
        Order order = getOne(lambdaQuery().eq(Order::getOrderId, orderId));
        if (order != null) {
            order.setStatus(OrderStatus.fromCode(status));
            order.setUpdateTime(LocalDateTime.now());
            return updateById(order);
        }
        return false;
    }


    // 分页查询订单信息
    @Override
    public PageDTO<OrderInfoVo> getAllOrders(Integer pageSize, Integer pageNum) {
        // 获取用户信息
        Long userId = UserContextUtil.getUserId();

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
    public PageDTO<OrderInfoVo> searchOrders(Integer pageSize, Integer pageNum, @NotNull SearchOrderDto seatchOrderDto) {
        // 获取用户信息
        Long userId = UserContextUtil.getUserId();

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
            orderInfoVo.setOrderId(order.getOrderId());
            orderInfoVo.setStatus(order.getStatus());
            orderInfoVo.setUserCurrency(order.getUserCurrency());
            orderInfoVo.setEmail(order.getEmail());
            orderInfoVo.setCreateTime(order.getCreateTime());
            orderInfoVo.setUpdateTime(order.getUpdateTime());

            //查询地址信息
            Address byId = iAddressService.getById(order.getAddressId());
            orderInfoVo.setAddress(byId);

            //查询商品信息
            String orderItems = order.getOrderItems();
            //解析json字符串,获取商品id列表
            String[] split = orderItems.split(",");
            //创建map，存储商品id和数量
            Map<Long, Integer> productIdCountMap = new HashMap<>();
            for (String s : split) {
                Long productId = Long.valueOf(s);
                productIdCountMap.put(productId, productIdCountMap.getOrDefault(productId, 0) + 1);
            }
            //遍历商品id列表，查询商品信息
            for (String s : split) {
                Long productId = Long.valueOf(s);
                //根据商品id查询商品信息
                ProductInfoVo data = productClient.getProductInfoById(productId).getData();
                //封装CartItem
                CartItem cartItem = new CartItem();
                cartItem.setProductId(productId);
                cartItem.setQuantity(productIdCountMap.get(productId));
                orderInfoVo.getCartItems().add(cartItem);

            }
            return orderInfoVo;
        }
        return null;
    }

}
