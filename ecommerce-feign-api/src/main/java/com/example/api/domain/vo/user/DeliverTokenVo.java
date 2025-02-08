package com.example.api.domain.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分发令牌实体")
public class DeliverTokenVo {
    @ApiModelProperty("令牌")
    private String token;
}
