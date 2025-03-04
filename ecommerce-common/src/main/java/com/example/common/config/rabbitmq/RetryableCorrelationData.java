package com.example.common.config.rabbitmq;

import lombok.Getter;
import lombok.Setter;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;

@Getter
@Setter
public class RetryableCorrelationData extends CorrelationData {
    private int retryCount;
    private final Object message;
    private final String exchange;
    private final String routingKey;
    private final MessagePostProcessor messagePostProcessor;

    public RetryableCorrelationData(Object message, String exchange, String routingKey, MessagePostProcessor messagePostProcessor) {
        this.retryCount = 0;
        this.message = message;
        this.exchange = exchange;
        this.routingKey = routingKey;
        this.messagePostProcessor = messagePostProcessor;
    }

    public RetryableCorrelationData(Object message, String exchange, String routingKey) {
        this.retryCount = 0;
        this.message = message;
        this.exchange = exchange;
        this.routingKey = routingKey;
        this.messagePostProcessor = null;
    }
}
