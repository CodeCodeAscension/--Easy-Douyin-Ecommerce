package com.example.common.config;

import com.example.common.config.rabbitmq.RetryableCorrelationData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
@AllArgsConstructor
@Slf4j
@ConditionalOnClass(RabbitTemplate.class)
public class RabbitInitConfig {
    private final RabbitTemplate rabbitTemplate;

    // 初始化rabbitTemplate
    @EventListener(ApplicationReadyEvent.class)
    public void initRabbitTemplate() {
        rabbitTemplate.setEncoding("UTF-8");
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if(ack) {
                return;
            }
            log.error("消息发送NACK：{}", cause);
            if(correlationData instanceof RetryableCorrelationData) {
                // 重试次数大于了5次以上
                if(((RetryableCorrelationData) correlationData).getRetryCount() >= 5) {

                } else {

                }
            }
        });
    }

    @Bean
    public MessageRecoverer republishMessageRecoverer() {
        return new RepublishMessageRecoverer(rabbitTemplate, "error.direct", "error");
    }
}
