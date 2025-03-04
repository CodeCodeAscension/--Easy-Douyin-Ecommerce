package com.example.api.domain.vo.order;

import com.example.api.domain.po.OrderResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "创建订单结果")
public class PlaceOrderVo {
    @Schema(description = "订单信息")
    private OrderResult order;
}
