package com.example.api.domain.vo.order;

import com.example.api.domain.po.Order;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "获取订单信息结果")
public class ListOrderVo {
    @ApiModelProperty("订单信息")
    private List<Order> orders;
}
