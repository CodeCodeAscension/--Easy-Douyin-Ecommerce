package com.example.common.config.rabbitmq;

import lombok.Data;

@Data
public class PayQueue {
    public String start;
    public String cancel;
    public String fail;
    public String success;
}
