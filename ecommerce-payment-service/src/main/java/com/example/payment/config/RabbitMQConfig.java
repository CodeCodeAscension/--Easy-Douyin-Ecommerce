package com.example.payment.config;

import com.example.common.config.rabbitmq.RabbitQueuesConfig;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
@EnableConfigurationProperties(RabbitQueuesConfig.class)
public class RabbitMQConfig {

    @Resource
    private RabbitQueuesConfig config;

    public RabbitMQConfig(RabbitQueuesConfig config) {
        this.config = config;
    }

    // ================== Fanout交换器声明 ==================
    @Bean
    public FanoutExchange paySuccessExchange() {
        return new FanoutExchange(config.getQueues().getPay().getSuccess());
    }

    @Bean
    public FanoutExchange payFailExchange() {
        return new FanoutExchange(config.getQueues().getPay().getFail());
    }

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
                .to(paySuccessExchange());
    }

    @Bean
    public Binding bindFailProduct() {
        return BindingBuilder.bind(payFailProductQueue())
                .to(payFailExchange());
    }

    // ================== 消息转换器 ==================
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}