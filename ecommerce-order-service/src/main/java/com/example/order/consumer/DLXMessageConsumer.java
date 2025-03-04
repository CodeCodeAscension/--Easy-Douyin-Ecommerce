package com.example.order.consumer;

import com.example.api.domain.vo.order.OrderInfoVo;
import com.example.api.enums.OrderStatusEnum;
import com.example.order.config.RabbitMQDLXConfig;
import com.example.order.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DLXMessageConsumer {

    private static final Logger logger = LoggerFactory.getLogger(DLXMessageConsumer.class);

    private final IOrderService iOrderService;

    /**
     * 消费死信队列中的消息
     *
     * @param message 死信队列中的消息
     */
    @RabbitListener(queues = RabbitMQDLXConfig.DLX_QUEUE)
    public void handleDlxMessage(@NotNull Message message) {
        try {
            // 获取消息内容
            String messageBody = new String(message.getBody()).replace("\"", "");

            // 获取订单信息
            OrderStatusEnum status = iOrderService.getOrderStatus(messageBody);
            if(status != OrderStatusEnum.WAIT_FOR_CONFIRM && status != OrderStatusEnum.WAIT_FOR_PAY) {
                logger.info("Order {} is already paid or canceled.", messageBody);
                return;
            }

            // 修改订单状态为已取消
            iOrderService.changeOrderStatus(messageBody, OrderStatusEnum.CANCELED);
        } catch (Exception e) {
            logger.error("Error processing dead letter message", e);
        }
    }
}
