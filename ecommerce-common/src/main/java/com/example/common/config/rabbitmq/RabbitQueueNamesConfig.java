package com.example.common.config.rabbitmq;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@ConfigurationProperties(prefix = "ecommerce.rabbitmq")
@Component
public class RabbitQueueNamesConfig {
    public String exchangeName;
    public Queues queues;
}