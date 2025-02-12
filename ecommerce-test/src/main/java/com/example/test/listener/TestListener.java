package com.example.test.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TestListener {
    @RabbitListener(queues = "test.queue")
    public void receive(String message) {
        System.out.println(message);
    }
}
