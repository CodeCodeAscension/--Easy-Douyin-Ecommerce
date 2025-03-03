package com.example.order.service.impl;

import com.alibaba.nacos.api.remote.response.ResponseCode;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.api.client.CartClient;
import com.example.api.client.ProductClient;
import com.example.api.domain.po.CartItem;
import com.example.api.domain.vo.product.ProductInfoVo;
import com.example.common.domain.ResponseResult;
import com.example.common.domain.ResultCode;
import com.example.common.exception.SystemException;
import com.example.order.domain.po.OrderItem;
import com.example.order.enums.OrderItemStatusEnum;
import com.example.order.mapper.OrderItemMapper;
import com.example.order.service.IOrderItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements IOrderItemService {

    private final ProductClient productClient;
    private final CartClient cartClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderItemByOrderId(String orderId, List<CartItem> orderItems) {
        orderItems.forEach(cartItem -> {
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
                        .status(OrderItemStatusEnum.PENDING)
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .build();
                this.save(orderItem);
            }
            // 更新价格
            orderItem.setCost(cost);
            orderItem.setUpdateTime(LocalDateTime.now());
            this.updateById(orderItem);
        });
    }

    @Override
    public List<CartItem> getCartItemsByOrderId(String orderId) {
        return this.list(Wrappers.<OrderItem>lambdaQuery()
                .eq(OrderItem::getOrderId, orderId))
                .stream().map(OrderItem::getCartItemId)
                .map(id -> {
                   ResponseResult<CartItem> item = cartClient.getCartItem(id);
                   if(item.getCode() != ResultCode.SUCCESS || item.getData() == null) {
                       log.error("CartService异常：{}", item.getMsg());
                       throw new SystemException(item.getMsg());
                   }
                   return item.getData();
                }).toList();
    }
}
