package com.example.api.client;

import com.example.api.domain.dto.order.ListOrderDto;
import com.example.api.domain.dto.order.MarkOrderPaidDto;
import com.example.api.domain.dto.order.PlaceOrderDto;
import com.example.api.domain.vo.order.ListOrderVo;
import com.example.api.domain.vo.order.MarkOrderPaidVo;
import com.example.api.domain.vo.order.PlaceOrderVo;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("order-service")
public interface OrderClient {
    PlaceOrderVo placeOrder(PlaceOrderDto placeOrderDto);
    ListOrderVo listOrder(ListOrderDto listOrderDto);
    MarkOrderPaidVo markOrderPaid(MarkOrderPaidDto markOrderPaidDto);
}
