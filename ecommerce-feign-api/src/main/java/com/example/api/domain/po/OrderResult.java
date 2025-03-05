package com.example.api.domain.po;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "创建订单结果")
public class OrderResult implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Schema(description = "订单ID")
    private String orderId;
}
