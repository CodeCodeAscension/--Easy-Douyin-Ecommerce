package com.example.cart.domain.dto;

//import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NonNull;

@Data
@Schema(description = "添加购物车DTO")
public class AddItemDTO {


    @NonNull
    @Schema(description = "商品id")
    private Long productId;

    @NonNull
    @Schema(description = "商品数量")
    private Integer quantity;

}
