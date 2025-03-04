package com.example.cart.consumer;

import com.example.api.client.OrderClient;
import com.example.api.domain.po.CartItem;
import com.example.api.domain.vo.order.OrderInfoVo;
import com.example.cart.service.ICartItemService;
import com.example.common.domain.ResponseResult;
import com.example.common.domain.ResultCode;
import com.example.common.domain.message.PaySuccessMessage;
import com.example.common.exception.SystemException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
public class PaySuccessConsumer {

    private final OrderClient orderClient;
    private final ICartItemService cartItemService;

    /**
     * 接听支付成功的消息，删去购物车项目
     * @param message 消息
     */
    @RabbitListener(queues = "pay.success.cart")
    public void paySuccess(PaySuccessMessage message) {
        String orderId = message.getOrderId();
        ResponseResult<OrderInfoVo> orderInfos = orderClient.getOrderById(orderId);
        if(orderInfos.getCode() != ResultCode.SUCCESS || orderInfos.getData() == null) {
            log.error("订单服务异常，无法进行清空购物车信息：{}", orderInfos.getMsg());
            throw new SystemException(orderInfos.getMsg());
        }
        // 删除购物车信息
        cartItemService.removeBatchByIds(
            orderInfos.getData().getCartItems().stream()
                    .map(CartItem::getCartItemId)
                    .collect(Collectors.toSet())
        );
    }
}
