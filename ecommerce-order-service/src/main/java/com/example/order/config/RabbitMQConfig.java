package com.example.order.config;

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

    // ================== 库存相关队列声明 ==================
    @Bean
    public Queue paySuccessProductQueue() {
        return QueueBuilder.durable(config.getQueues().getPay().getSuccess().concat(".order"))
                .lazy()
                .build();
    }

    @Bean
    public Queue payFailProductQueue() {
        return QueueBuilder.durable(config.getQueues().getPay().getFail().concat(".order"))
                .lazy()
                .build();
    }

    @Bean
    public Queue payCancelProductQueue() {
        return QueueBuilder.durable(config.getQueues().getPay().getCancel().concat(".order"))
                .lazy()
                .build();
    }

    @Bean
    public Queue payStartProductQueue() {
        return QueueBuilder.durable(config.getQueues().getPay().getStart().concat(".order"))
                .lazy()
                .build();
    }

    // ================== 队列绑定交换器 ==================
    @Bean
    public Binding bindSuccessProduct() {
        return BindingBuilder.bind(paySuccessProductQueue())
                .to(exchangeConfig.paySuccessFanoutExchange());
    }

    @Bean
    public Binding bindFailProduct() {
        return BindingBuilder.bind(payFailProductQueue())
                .to(exchangeConfig.payFailFanoutExchange());
    }

    @Bean
    public Binding bindStartProduct() {
        return BindingBuilder.bind(payStartProductQueue())
                .to(exchangeConfig.payStartFanoutExchange());
    }

    @Bean
    public Binding bindCancelProduct() {
        return BindingBuilder.bind(payCancelProductQueue())
                .to(exchangeConfig.payCancelFanoutExchange());
    }
}
