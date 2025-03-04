package com.example.cart.config;

import com.example.common.config.rabbitmq.RabbitQueueNamesConfig;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class RabbitMQConfig {

    private final RabbitQueueNamesConfig config;

    @Bean
    public Queue paySuccessProductQueue() {
        return QueueBuilder.durable(config.getQueues().getPay().getSuccess().concat(".cart"))
                .lazy()
                .build();
    }
}
