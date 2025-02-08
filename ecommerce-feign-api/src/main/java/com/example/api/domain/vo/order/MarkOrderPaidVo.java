package com.example.api.domain.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "标记订单已支付结果")
public class MarkOrderPaidVo {
    @ApiModelProperty("是否成功")
    private Boolean res;
}
