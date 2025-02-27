package com.example.api.domain.dto.order;

import com.example.api.enums.OrderStatus;
import lombok.Data;

import java.util.Date;

@Data
public class SearchOrderDto {
    private Date paymentDateUpperBound;     // 支付时间上界（包含）
    private Date paymentDateLowerBound;     // 支付时间下界（包含）
    private Date createDateUpperBound;      // 创建时间上界（包含）
    private Date createDateLowerBound;      // 创建时间下界（包含）
    private OrderStatus status;             // 订单状态
}
