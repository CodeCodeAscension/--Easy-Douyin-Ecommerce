package com.example.api.domain.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "收获地址实体类")
public class Address {
    @ApiModelProperty("街道地址")
    private String streetAddress;
    @ApiModelProperty("城市")
    private String city;
    @ApiModelProperty("省份")
    private String province;
    @ApiModelProperty("国家")
    private String country;
    @ApiModelProperty("邮政编码")
    private String zipCode;
}
