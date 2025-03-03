package com.example.payment.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 定义订单自动取消的延迟队列
 */
@Configuration
public class RabbitMQTimeoutConfig {
    public static final String EXCHANGE_NAME = "payment.timeout.exchange";
    public static final String ROUTING_KEY = "payment.timeout";
    public static final String QUEUE_NAME = "payment.timeout.queue";
    public static final String DLX_QUEUE_NAME = "payment.timeout.dlx.queue";
    public static final String DLX_EXCHANGE_NAME = "payment.timeout.dlx.exchange";

    @Bean
    public DirectExchange originExchange() {
        return ExchangeBuilder.directExchange(EXCHANGE_NAME)
                .durable(true)
                .build();
    }

    @Bean
    public Queue originQueue() {
        return QueueBuilder.durable(QUEUE_NAME)
                .deadLetterExchange(DLX_EXCHANGE_NAME)
                .deadLetterRoutingKey(ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding originBinding() {
        return BindingBuilder.bind(originQueue())
                .to(originExchange())
                .with(ROUTING_KEY);
    }

    @Bean
    public DirectExchange dlxExchange() {
        return ExchangeBuilder.directExchange(DLX_EXCHANGE_NAME)
                .durable(true)
                .build();
    }

    @Bean
    public Queue dlxQueue() {
        return QueueBuilder.durable(DLX_QUEUE_NAME).build();
    }

    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(dlxQueue())
                .to(dlxExchange())
                .with(ROUTING_KEY);
    }
}
