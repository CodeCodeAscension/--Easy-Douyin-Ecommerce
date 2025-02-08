package com.example.api.domain.dto.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "清空购物车请求")
public class EmptyCartDto {
    @ApiModelProperty("用户ID")
    private Long userId;
}
