package com.example.api.domain.po;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "创建订单结果")
public class OrderResult {
    @Schema(description = "订单ID")
    private String orderId;
}
