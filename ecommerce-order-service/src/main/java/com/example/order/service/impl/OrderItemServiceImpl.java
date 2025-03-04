package com.example.order.service.impl;

import com.alibaba.nacos.api.remote.response.ResponseCode;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.api.client.CartClient;
import com.example.api.client.ProductClient;
import com.example.api.domain.po.CartItem;
import com.example.api.domain.vo.product.ProductInfoVo;
import com.example.api.enums.OrderStatusEnum;
import com.example.common.domain.ResponseResult;
import com.example.common.domain.ResultCode;
import com.example.common.exception.BadRequestException;
import com.example.common.exception.NotFoundException;
import com.example.common.exception.SystemException;
import com.example.order.domain.po.OrderItem;
import com.example.order.mapper.OrderItemMapper;
import com.example.order.service.IOrderItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements IOrderItemService {

    private final ProductClient productClient;
    private final CartClient cartClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderItemByOrderId(String orderId, List<CartItem> orderItems) {
        if(orderItems == null || orderItems.isEmpty()) {
            return;
        }
        orderItems.forEach(cartItem -> {
            // 判断购物车物品是否存在
            ResponseResult<CartItem> exists = cartClient.getCartItem(cartItem.getCartItemId());
            if(exists.getCode() == ResultCode.NOT_FOUND) {
                throw new NotFoundException("指定购物车项目不存在");
            } else if(exists.getCode() != ResultCode.SUCCESS) {
                log.error("cart-service异常：{}", exists.getMsg());
                throw new SystemException("cart-service异常");
            }
            if(!Objects.equals(cartItem.getProductId(), exists.getData().getProductId()) || !Objects.equals(cartItem.getQuantity(), exists.getData().getQuantity())) {
                throw new BadRequestException("下单数据与购物车数据不符合");
            }
            Float cost = 0f;
            // 从商品服务计算总价格
            ResponseResult<ProductInfoVo> resp = productClient.getProductInfoById(cartItem.getProductId());
            if(resp.getCode() != ResultCode.SUCCESS || resp.getData() == null) {
                log.error("商品服务出错：{}", resp.getMsg());
                throw new SystemException(resp.getMsg());
            }
            cost = cartItem.getQuantity() * resp.getData().getPrice();
            // 获取OrderItem信息
            OrderItem orderItem = getOne(Wrappers.<OrderItem>lambdaQuery()
                    .eq(OrderItem::getOrderId, orderId)
                    .eq(OrderItem::getCartItemId, cartItem.getCartItemId())
            );
            // 如果没有则创建
            if (orderItem == null) {
                orderItem = OrderItem.builder()
                        .orderId(orderId)
                        .cartItemId(cartItem.getCartItemId())
                        .cost(0f)
                        .quantity(cartItem.getQuantity())
                        .productId(cartItem.getProductId())
                        .status(OrderStatusEnum.WAIT_FOR_PAY)
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .build();
                this.save(orderItem);
            }
            // 更新价格
            orderItem.setCost(cost);
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUpdateTime(LocalDateTime.now());
            this.updateById(orderItem);
        });
    }

    @Override
    public List<CartItem> getCartItemsByOrderId(String orderId) {
        return this.list(Wrappers.<OrderItem>lambdaQuery()
                .eq(OrderItem::getOrderId, orderId))
                .stream()
                .map(item -> new CartItem(item.getCartItemId(), item.getProductId(), item.getQuantity()))
                .toList();
    }

    @Transactional
    @Override
    public void setOrderItemsStatus(String orderId, OrderStatusEnum status) {
        this.list(Wrappers.<OrderItem>lambdaQuery().eq(OrderItem::getOrderId, orderId))
                .forEach(orderItem -> {
                   orderItem.setStatus(status);
                   orderItem.setUpdateTime(LocalDateTime.now());
                   if(status == OrderStatusEnum.PAID) {
                       orderItem.setPayTime(LocalDateTime.now());
                   }
                   this.updateById(orderItem);
                });
    }
}
