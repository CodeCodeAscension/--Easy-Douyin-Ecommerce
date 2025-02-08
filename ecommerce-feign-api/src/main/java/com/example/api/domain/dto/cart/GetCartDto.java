package com.example.api.domain.dto.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "获得购物车信息请求")
public class GetCartDto {
    @ApiModelProperty("用户ID")
    private Long userId;
}
