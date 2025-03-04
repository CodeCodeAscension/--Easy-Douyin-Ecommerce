package com.example.api.client;

import com.example.api.client.fallback.OrderClientFallBack;
import com.example.api.domain.dto.order.*;
import com.example.api.domain.vo.order.OrderInfoVo;
import com.example.api.domain.vo.order.PlaceOrderVo;
import com.example.common.domain.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "order-service", fallbackFactory = OrderClientFallBack.class)
public interface OrderClient {
    // 当前用户下订单
    @PostMapping("/api/v1/orders")
    ResponseResult<PlaceOrderVo> placeOrder(@RequestBody PlaceOrderDto placeOrderDto);

    // 获得当前用户所有订单
    @GetMapping("/api/v1/orders")
    ResponseResult<List<OrderInfoVo>> getAllOrders();

    // 获得用户某次订单
    @GetMapping("/api/v1/orders/{orderId}")
    ResponseResult<OrderInfoVo> getOrderById(@PathVariable("orderId") String orderId);

    // 根据条件查询用户订单
    @GetMapping("/api/v1/orders/search")
    ResponseResult<List<OrderInfoVo>> searchOrders(@RequestBody SearchOrderDto searchOrderDto);
}
