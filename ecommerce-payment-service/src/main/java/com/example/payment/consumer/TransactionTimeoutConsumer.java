package com.example.payment.consumer;

import com.example.common.exception.DatabaseException;
import com.example.payment.config.RabbitMQTimeoutConfig;
import com.example.payment.domain.po.Transaction;
import com.example.api.enums.TransactionStatusEnum;
import com.example.payment.service.TransactionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class TransactionTimeoutConsumer {
    private final TransactionService transactionService;

    /**
     * 自动取消支付
     * @param message 死信队列的消息
     */
    @RabbitListener(queues = RabbitMQTimeoutConfig.DLX_QUEUE_NAME)
    public void receive(Message message) {
        log.info("收到订单超时信息： {}", message);
        String tranId = new String(message.getBody());
        tranId = tranId.replace("\"", "");
        transactionService.cancelCharge(null, tranId);
    }
}
