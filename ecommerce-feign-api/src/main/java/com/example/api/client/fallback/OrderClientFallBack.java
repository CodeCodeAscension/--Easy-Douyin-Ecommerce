package com.example.api.client.fallback;

import com.example.api.client.OrderClient;
import com.example.api.domain.dto.order.*;
import com.example.api.domain.vo.order.OrderInfoVo;
import com.example.api.domain.vo.order.PlaceOrderVo;
import com.example.common.domain.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class OrderClientFallBack implements FallbackFactory<OrderClient> {
    @Override
    public OrderClient create(Throwable cause) {
        return new OrderClient() {
            @Override
            public ResponseResult<PlaceOrderVo> placeOrder(PlaceOrderDto placeOrderDto) {
                log.error("order-service-exception:placeOrder, {}", cause.getMessage());
                return ResponseResult.errorFeign(cause);
            }

            @Override
            public ResponseResult<List<OrderInfoVo>> getAllOrders() {
                log.error("order-service-exception:getAllOrders, {}", cause.getMessage());
                return ResponseResult.errorFeign(cause);
            }

            @Override
            public ResponseResult<OrderInfoVo> getOrderById(String orderId) {
                log.error("order-service-exception:getOrderById, {}", cause.getMessage());
                return ResponseResult.errorFeign(cause);
            }

            @Override
            public ResponseResult<List<OrderInfoVo>> searchOrders(SearchOrderDto searchOrderDto) {
                log.error("order-service-exception:searchOrders, {}", cause.getMessage());
                return ResponseResult.errorFeign(cause);
            }
        };
    }
}
