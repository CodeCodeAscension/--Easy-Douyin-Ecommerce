package com.example.api.client;

import com.example.api.client.fallback.OrderClientFallBack;
import com.example.api.domain.dto.order.*;
import com.example.api.domain.vo.order.OrderInfoVo;
import com.example.api.domain.vo.order.PlaceOrderVo;
import com.example.api.enums.OrderStatus;
import com.example.common.domain.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "order-service", fallbackFactory = OrderClientFallBack.class)
public interface OrderClient {
    // 当前用户下订单
    @PostMapping("/api/v1/orders")
    ResponseResult<PlaceOrderVo> placeOrder(@RequestBody PlaceOrderDto placeOrderDto);

    // 更新当前用户的订单
    @PutMapping("/api/v1/orders")
    ResponseResult<Object> updateOrder(@RequestBody UpdateOrderDto updateOrderDto);

    // 设置自动取消订单
    @DeleteMapping("/api/v1/orders")
    ResponseResult<Object> autoCancelOrder(@RequestBody CancelOrderDto cancelOrderDto);

    // 获得当前用户所有订单
    @GetMapping("/api/v1/orders")
    ResponseResult<List<OrderInfoVo>> getAllOrders();

    // 获得用户某次订单
    @GetMapping("/api/v1/orders/{orderId}")
    ResponseResult<OrderInfoVo> getOrderById(@PathVariable("orderId") String orderId);

    // 订单标记为已支付
    @PostMapping("/api/v1/orders/paid")
    ResponseResult<Object> markOrderPaid(@RequestBody MarkOrderPaidDto markOrderPaidDto);


    @GetMapping("/api/v1/orders/search")
    ResponseResult<List<OrderInfoVo>> searchOrders(@RequestBody SearchOrderDto searchOrderDto);
}
