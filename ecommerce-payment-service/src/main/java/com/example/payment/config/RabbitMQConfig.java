package com.example.payment.config;

import com.example.common.config.RabbitMQExchangeConfig;
import com.example.common.config.rabbitmq.RabbitQueueNamesConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
@RequiredArgsConstructor
//@EnableConfigurationProperties(RabbitQueuesConfig.class)
public class RabbitMQConfig {

    private final RabbitQueueNamesConfig config;
    private final RabbitMQExchangeConfig exchangeConfig;

    // ================== 库存相关队列声明 ==================
    @Bean
    public Queue paySuccessProductQueue() {
        return new Queue("pay.success.product", true);
    }

    @Bean
    public Queue payFailProductQueue() {
        return new Queue("pay.fail.product", true);
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
}