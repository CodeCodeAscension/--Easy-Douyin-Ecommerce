package com.example.order.config;

import lombok.Getter;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@Getter
public class RabbitMQDLXConfig {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQDLXConfig.class);

    // 定义常量

    public static final String ORDER_QUEUE = "order.queue";    // 原始队列名
    public static final String ORDER_EXCHANGE = "order.exchange";  // 原始交换机名
    public static final String ORDER_ROUTING_KEY = "order.create";  // 原始路由键
    public static final String DLX_EXCHANGE = "order.dlx.exchange";  // 死信交换机名
    public static final String DLX_QUEUE = "order.dlx.queue";  // 死信队列名
    public static final String DLX_ROUTING_KEY = "order.cancel";  // 死信路由键


    // 定义原始交换机（普通直连交换机）
    @Bean
    public DirectExchange orderExchange() {
        logger.info("Creating order exchange: {}", ORDER_EXCHANGE);
        return ExchangeBuilder.directExchange(ORDER_EXCHANGE)
                .durable(true)
                .build();
    }

    // 定义原始队列（需绑定死信参数）
    @Bean
    public Queue orderQueue() {
        logger.info("Creating order queue with dead letter exchange and routing key"); // 创建原始队列时，指定死信交换机和路由键
        return QueueBuilder.durable(ORDER_QUEUE)
                .deadLetterExchange(DLX_EXCHANGE)
                .deadLetterRoutingKey(DLX_ROUTING_KEY)
                .build();
    }

    // 绑定原始队列到交换机
    @Bean
    public Binding orderBinding(Queue orderQueue, DirectExchange orderExchange) {
        logger.info("Binding order queue to exchange with routing key: {}", ORDER_ROUTING_KEY); // 绑定原始队列到交换机时，指定路由键
        return BindingBuilder.bind(orderQueue)
                .to(orderExchange)
                .with(ORDER_ROUTING_KEY);
    }

    // 定义死信交换机
    @Bean
    public DirectExchange dlxExchange() {
        logger.info("Creating dead letter exchange: {}", DLX_EXCHANGE); // 创建死信交换机
        return ExchangeBuilder.directExchange(DLX_EXCHANGE)
                .durable(true)
                .build();
    }

    // 定义死信队列（实际处理取消的队列）
    @Bean
    public Queue dlxQueue() {
        logger.info("Creating dead letter queue: {}", DLX_QUEUE); // 创建死信队列
        return QueueBuilder.durable(DLX_QUEUE).build();
    }

    // 绑定死信队列到死信交换机
    @Bean
    public Binding dlxBinding(Queue dlxQueue, DirectExchange dlxExchange) {
        logger.info("Binding dead letter queue to exchange with routing key: {}", DLX_ROUTING_KEY); // 绑定死信队列到死信交换机时，指定路由键
        return BindingBuilder.bind(dlxQueue)
                .to(dlxExchange)
                .with(DLX_ROUTING_KEY);
    }

}
