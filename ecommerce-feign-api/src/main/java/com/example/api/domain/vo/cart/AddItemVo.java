package com.example.api.domain.vo.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "添加商品结果")
public class AddItemVo {
    @ApiModelProperty("是否成功")
    private Boolean res;
}
