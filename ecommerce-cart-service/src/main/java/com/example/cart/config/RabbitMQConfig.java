package com.example.cart.config;

import com.example.common.config.RabbitMQExchangeConfig;
import com.example.common.config.rabbitmq.RabbitQueueNamesConfig;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class RabbitMQConfig {

    private final RabbitQueueNamesConfig config;
    private final RabbitMQExchangeConfig exchangeConfig;

    @Bean
    public Queue paySuccessCartQueue() {
        return QueueBuilder.durable(config.getQueues().getPay().getSuccess().concat(".cart"))
                .lazy()
                .build();
    }

    @Bean
    public Binding bindSuccessCart() {
        return BindingBuilder.bind(paySuccessCartQueue())
                .to(exchangeConfig.paySuccessFanoutExchange());
    }
}
