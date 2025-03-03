package com.example.common.config.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PayQueue {
    public String start;
    public String cancel;
    public String fail;
    public String success;
}
