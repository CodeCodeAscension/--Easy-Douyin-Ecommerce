package com.example.api.client.fallback;

import com.example.api.client.OrderClient;
import com.example.api.domain.dto.order.CancelOrderDto;
import com.example.api.domain.dto.order.MarkOrderPaidDto;
import com.example.api.domain.dto.order.PlaceOrderDto;
import com.example.api.domain.dto.order.UpdateOrderDto;
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
                log.error("order-service-exception:placeOrder, "+cause.getMessage());
                return ResponseResult.errorFeign(cause);
            }

            @Override
            public ResponseResult<Object> updateOrder(UpdateOrderDto updateOrderDto) {
                log.error("order-service-exception:updateOrder, "+cause.getMessage());
                return ResponseResult.errorFeign(cause);
            }

            @Override
            public ResponseResult<Object> autoCancelOrder(CancelOrderDto cancelOrderDto) {
                log.error("order-service-exception:autoCancelOrder, "+cause.getMessage());
                return ResponseResult.errorFeign(cause);
            }

            @Override
            public ResponseResult<List<OrderInfoVo>> getAllOrders() {
                log.error("order-service-exception:getAllOrders, "+cause.getMessage());
                return ResponseResult.errorFeign(cause);
            }

            @Override
            public ResponseResult<OrderInfoVo> getOrderById(String orderId) {
                log.error("order-service-exception:getOrderById, "+cause.getMessage());
                return ResponseResult.errorFeign(cause);
            }

            @Override
            public ResponseResult<Object> markOrderPaid(MarkOrderPaidDto markOrderPaidDto) {
                log.error("order-service-exception:markOrderPaid, "+cause.getMessage());
                return ResponseResult.errorFeign(cause);
            }
        };
    }
}
