package com.example.ai.aiUtil;

import com.example.api.client.OrderClient;
import com.example.api.domain.po.Address;
import com.example.api.domain.vo.order.OrderInfoVo;
import com.example.common.domain.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class OrderUtil {
    //private final OrderClient orderClient;

    // 获取订单信息
    @Tool(description = "你可以通过这个方法获取用户所有的订单信息,当用户问你他的订单信息时,你可以把方法结果返回")
    String getAllOrders() {
        List<OrderInfoVo> mockOrders = new ArrayList<>();

        // 添加模拟订单数据
        for (int i = 1; i <= 4; i++) {
            OrderInfoVo order = new OrderInfoVo();
            order.setOrderId("order" + i);
            order.setStatus(i % 2 == 0 ? 1 : 2);
            order.setUserCurrency("CNY");
            order.setEmail("user" + i + "@example.com");
            Address address = new Address();
            address.setStreetAddress("Address " + i);
            address.setCity("City " + i);
            address.setProvince("Province " + i);
            address.setCountry("Country " + i);
            address.setZipCode("ZipCode " + i);
            order.setAddress(address);
            order.setCreateTime(LocalDateTime.now());
            order.setUpdateTime(LocalDateTime.now());
            mockOrders.add(order);

        }

        ResponseResult<List<OrderInfoVo>> allOrders =ResponseResult.success(mockOrders);
        return allOrders.getData().toString();
    }

    // 生成模拟订单数据

}
