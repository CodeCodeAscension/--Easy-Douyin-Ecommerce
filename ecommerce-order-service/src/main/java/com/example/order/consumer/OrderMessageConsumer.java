package com.example.order.consumer;

import com.example.api.enums.OrderStatusEnum;
import com.example.common.domain.message.PayCancelMessage;
import com.example.common.domain.message.PayFailMessage;
import com.example.common.domain.message.PayStartMessage;
import com.example.common.domain.message.PaySuccessMessage;
import com.example.order.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class OrderMessageConsumer {

    private static final Logger logger = LoggerFactory.getLogger(OrderMessageConsumer.class);

    private final IOrderService iorderService;

    // 处理支付成功
    @RabbitListener(queues = "pay.success.order") // 监听支付成功队列
    public void handlePaymentSuccess(PaySuccessMessage message) {
        try {
            logger.info("收到支付成功消息：{}", message);
            String messageBody = message.getOrderId();
            if(iorderService.getOrderStatus(messageBody) != OrderStatusEnum.WAIT_FOR_PAY) {
                logger.info("订单状态不正确，放弃本条信息");
                return;
            }
            //修改订单信息为以支付成功
            iorderService.changeOrderStatus(messageBody, OrderStatusEnum.PAID);
            logger.info("订单处理成功：{}", messageBody);
        } catch (Exception e) {
            logger.error("处理支付成功消息异常：", e);
            throw new RuntimeException(e);
        }
    }

    // 处理支付失败
    @RabbitListener(queues ="pay.fail.order")
    public void handlePaymentFailure(PayFailMessage message) {
        try {
            logger.info("收到支付失败消息：{}", message);
            //获取信息
            String messageBody = message.getOrderId();
            if(iorderService.getOrderStatus(messageBody) != OrderStatusEnum.WAIT_FOR_PAY) {
                logger.info("订单状态不正确，放弃本条信息");
                return;
            }
            //修改订单状态为支付失败
            iorderService.changeOrderStatus(messageBody, OrderStatusEnum.PAYMENT_FAIL);

            logger.info("修改订单状态完成：{}", messageBody);
        } catch (Exception e) {
            logger.error("处理支付失败消息异常：", e);
            throw new RuntimeException(e);
        }
    }

    // 处理取消支付
    @RabbitListener(queues = "pay.cancel.order")
    public void handlePaymentCancel(PayCancelMessage message) {
        try {
            logger.info("收到取消支付消息：{}", message);
            //获取信息
            String messageBody = message.getOrderId();
            OrderStatusEnum status = iorderService.getOrderStatus(messageBody);
            if(status != OrderStatusEnum.WAIT_FOR_CONFIRM && status != OrderStatusEnum.WAIT_FOR_PAY) {
                logger.info("订单状态不正确，放弃本条信息");
                return;
            }
            //修改订单状态为已取消
            iorderService.changeOrderStatus(messageBody, OrderStatusEnum.CANCELED);

            logger.info("订单已取消：{}", messageBody);
        } catch (Exception e) {
            logger.error("处理取消支付消息异常：", e);
            throw new RuntimeException(e);
        }
    }

    //处理待支付
    @RabbitListener(queues = "pay.start.order")
    public void handlePaymentStart(PayStartMessage message) {
        try {
            logger.info("收到待支付消息：{}", message);
            //获取信息
            String messageBody = message.getOrderId();
            if(iorderService.getOrderStatus(messageBody) != OrderStatusEnum.WAIT_FOR_CONFIRM) {
                logger.info("订单状态不正确，放弃本条信息");
                return;
            }
            //修改订单状态为待支付
            iorderService.changeOrderStatus(messageBody, OrderStatusEnum.WAIT_FOR_PAY);

            logger.info("订单已标记待支付：{}", messageBody);
        } catch (Exception e) {
            logger.error("处理待支付消息异常：", e);
            throw new RuntimeException(e);
        }
    }

}