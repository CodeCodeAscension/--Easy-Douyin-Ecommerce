package com.example.common.domain.message;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class PayStartMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String orderId;
}
