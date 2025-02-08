package com.example.api.domain.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "查询商品信息请求")
public class ListProductsDto {
    @ApiModelProperty("第几页")
    private Integer page;
    @ApiModelProperty("每页数量")
    private Long pageSize;
    @ApiModelProperty("类别")
    private String categoryName;
}
