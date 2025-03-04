package com.example.common.config;

import com.example.common.config.rabbitmq.RabbitQueueNamesConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 在此定义整个项目的交换机以及交换机之间的关系
 * 各个服务的队列与交换机的绑定关系则在各自微服务中定义
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnClass(RabbitTemplate.class)
@Slf4j
public class RabbitMQExchangeConfig {
    private final RabbitQueueNamesConfig config;

    // 配置JSON转换器
    @Bean
    public MessageConverter messageConverter(){
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        jackson2JsonMessageConverter.setCreateMessageIds(true);
        return jackson2JsonMessageConverter;
    }

    @Bean
    public DirectExchange directExchange() {
        return ExchangeBuilder.directExchange(config.getExchangeName())
                .durable(true)
                .build();
    }

    @Bean
    public FanoutExchange payStartFanoutExchange() {
        return ExchangeBuilder.fanoutExchange(config.getQueues().getPay().getStart())
                .durable(true)
                .build();
    }

    @Bean
    public FanoutExchange payCancelFanoutExchange() {
        return ExchangeBuilder.fanoutExchange(config.getQueues().getPay().getCancel())
                .durable(true)
                .build();
    }

    @Bean
    public FanoutExchange payFailFanoutExchange() {
        return ExchangeBuilder.fanoutExchange(config.getQueues().getPay().getFail())
                .durable(true)
                .build();
    }

    @Bean
    public FanoutExchange paySuccessFanoutExchange() {
        return ExchangeBuilder.fanoutExchange(config.getQueues().getPay().getSuccess())
                .durable(true)
                .build();
    }

    @Bean
    public Binding payStartBinding(DirectExchange directExchange, FanoutExchange payStartFanoutExchange) {
        return BindingBuilder.bind(payStartFanoutExchange)
                .to(directExchange)
                .with(config.getQueues().getPay().getStart());
    }

    @Bean
    public Binding payCancelBinding(DirectExchange directExchange, FanoutExchange payCancelFanoutExchange) {
        return BindingBuilder.bind(payCancelFanoutExchange)
                .to(directExchange)
                .with(config.getQueues().getPay().getCancel());
    }

    @Bean
    public Binding payFailBinding(DirectExchange directExchange, FanoutExchange payFailFanoutExchange) {
        return BindingBuilder.bind(payFailFanoutExchange)
                .to(directExchange)
                .with(config.getQueues().getPay().getFail());
    }

    @Bean
    public Binding paySuccessBinding(DirectExchange directExchange, FanoutExchange paySuccessFanoutExchange) {
        return BindingBuilder.bind(paySuccessFanoutExchange)
                .to(directExchange)
                .with(config.getQueues().getPay().getSuccess());
    }

    // 异常交换器和异常队列
    @Bean
    public DirectExchange errorMessageExchange(){
        return ExchangeBuilder.directExchange("error.direct")
                .durable(true)
                .build();
    }

    @Bean
    public Queue errorQueue(){
        return new Queue("error.queue", true);
    }

    @Bean
    public Binding errorBinding(Queue errorQueue, DirectExchange errorMessageExchange){
        return BindingBuilder.bind(errorQueue)
                .to(errorMessageExchange)
                .with("error");
    }
}
