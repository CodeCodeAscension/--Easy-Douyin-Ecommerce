package com.example.api.domain.dto.order;

import com.example.api.enums.OrderStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "订单查询条件DTO")
public class SearchOrderDto {
    @Schema(description = "支付时间上界（包含）")
    private Date paymentDateUpperBound;
    @Schema(description = "支付时间下界（包含）")
    private Date paymentDateLowerBound;
    @Schema(description = "创建时间上界（包含）")
    private Date createDateUpperBound;
    @Schema(description = "创建时间下界（包含）")
    private Date createDateLowerBound;
    @Schema(description = "订单状态")
    private OrderStatusEnum status;
}
