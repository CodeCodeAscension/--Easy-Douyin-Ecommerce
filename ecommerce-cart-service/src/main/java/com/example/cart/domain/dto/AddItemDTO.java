package com.example.cart.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

@Data
public class AddItemDTO {

    @NonNull
    @ApiModelProperty("商品id")
    private Long productId;

    @NonNull
    @ApiModelProperty("商品数量")
    private Integer quantity;

}
