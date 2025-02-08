package com.example.api.domain.vo.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "清空购物车信息结果")
public class EmptyCartVo {
    @ApiModelProperty("是否成功")
    private Boolean res;
}
