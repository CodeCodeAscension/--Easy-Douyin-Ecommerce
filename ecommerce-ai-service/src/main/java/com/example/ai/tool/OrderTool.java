package com.example.ai.tool;

import com.example.api.client.OrderClient;
import com.example.api.domain.vo.order.OrderInfoVo;
import com.example.common.domain.ResponseResult;
import com.example.common.domain.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderTool {

    private final OrderClient orderClient;

    @Tool(description = "你可以通过这个方法获取用户所有的订单信息，当用户问你他的订单信息时，你可以把方法结果返回")
    String getAllOrders() {
        ResponseResult<List<OrderInfoVo>> responseResult = orderClient.getAllOrders();
        if(responseResult.getCode() != ResultCode.SUCCESS || responseResult.getData() == null) {
            return "";
        }
        return responseResult.getData().toString();
    }
}
