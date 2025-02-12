package com.example.test;

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
        rabbitTemplate.convertAndSend("test.queue", "Hello World");
    }
}
