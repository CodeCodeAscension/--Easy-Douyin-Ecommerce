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
    private final OrderClient orderClient;

    // 获取订单信息
    @Tool(description = "你可以通过这个方法获取用户所有的订单信息,当用户问你他的订单信息时,你可以把方法结果返回")
    String getAllOrders() {
        return orderClient.getAllOrders().getData().toString();
    }

    // 生成模拟订单数据

}
