package com.example.test;

import com.example.common.config.rabbitmq.RabbitQueueNamesConfig;
import com.example.test.service.TestService;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestRun {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testRabbitMqSend() {
        rabbitTemplate.convertAndSend("order.dlx.queue", "1896984825336827905");
    }

    @Autowired
    private RabbitQueueNamesConfig rabbitQueueNamesConfig;

    @Test
    public void testRabbit() {
        rabbitTemplate.convertAndSend(rabbitQueueNamesConfig.exchangeName, rabbitQueueNamesConfig.queues.pay.start, "Hello World");
    }

    @Autowired
    private TestService testService;

    @Test
    public void testSeata() {
        testService.testSeata();
    }
}
