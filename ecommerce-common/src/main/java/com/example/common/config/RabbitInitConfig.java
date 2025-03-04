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
import org.springframework.data.redis.core.RedisTemplate;

/**
 * MQ生产者发送消息失败后，生产者重试
 */
@Configuration
@AllArgsConstructor
@Slf4j
@ConditionalOnClass(RabbitTemplate.class)
public class RabbitInitConfig {
    private final RabbitTemplate rabbitTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final String REDIS_ERROR_KEY = "ecommerce-mq-send-error";

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
                    // 存储到Redis中，进行人工处理
                    redisTemplate.opsForHash().put(REDIS_ERROR_KEY, correlationData.getId(), ((RetryableCorrelationData) correlationData).getMessage());
                } else {
                    // 进行消息重发
                    RetryableCorrelationData data = (RetryableCorrelationData) correlationData;
                    data.setRetryCount(data.getRetryCount() + 1);
                    int delay = (int) Math.pow(2, data.getRetryCount()) * 1000; // 指数退避
                    new Thread(() -> {
                        try {
                            Thread.sleep(delay);
                            if(data.getMessagePostProcessor() == null) {
                                rabbitTemplate.convertAndSend(
                                        data.getExchange(),
                                        data.getRoutingKey(),
                                        data.getMessage(),
                                        data
                                );
                            } else {
                                rabbitTemplate.convertAndSend(
                                        data.getExchange(),
                                        data.getRoutingKey(),
                                        data.getMessage(),
                                        data.getMessagePostProcessor(),
                                        data
                                );
                            }
                        } catch (InterruptedException ignored) {

                        }
                    }).start();
                }
            }
        });
    }

    @Bean
    public MessageRecoverer republishMessageRecoverer() {
        return new RepublishMessageRecoverer(rabbitTemplate, "error.direct", "error");
    }
}
