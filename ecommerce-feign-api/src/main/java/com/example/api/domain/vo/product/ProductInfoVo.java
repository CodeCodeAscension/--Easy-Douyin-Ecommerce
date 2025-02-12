package com.example.api.domain.vo.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(description = "商品信息显示")
public class ProductInfoVo {
    @ApiModelProperty("商品ID")
    private Long id;
    @ApiModelProperty("商品名称")
    private String name;
    @ApiModelProperty("商品描述")
    private String description;
    @ApiModelProperty("商品价格")
    private Float price;
    @ApiModelProperty("商品销量")
    private Integer sold;
    @ApiModelProperty("商品库存")
    private Integer stock;
    @ApiModelProperty("商家名称")
    private String merchantName;
    @ApiModelProperty("所属类别")
    private List<String> categories;
    @ApiModelProperty("状态（0上架，1下架）")
    private Integer status;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;
}
