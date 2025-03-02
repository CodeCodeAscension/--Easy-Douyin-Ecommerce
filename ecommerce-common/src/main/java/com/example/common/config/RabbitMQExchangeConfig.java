package com.example.common.config;

import com.example.common.config.rabbitmq.RabbitQueueNamesConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
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
@ConditionalOnClass({FanoutExchange.class, DirectExchange.class})
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
        return new DirectExchange(config.getExchangeName());
    }

    @Bean
    public FanoutExchange payStartFanoutExchange() {
        return new FanoutExchange(config.getQueues().getPay().getStart());
    }

    @Bean
    public FanoutExchange payCancelFanoutExchange() {
        return new FanoutExchange(config.getQueues().getPay().getCancel());
    }

    @Bean
    public FanoutExchange payFailFanoutExchange() {
        return new FanoutExchange(config.getQueues().getPay().getFail());
    }

    @Bean
    public FanoutExchange paySuccessFanoutExchange() {
        return new FanoutExchange(config.getQueues().getPay().getSuccess());
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
}
