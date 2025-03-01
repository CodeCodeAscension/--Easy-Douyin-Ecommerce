package com.example.product.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@Schema(description = "更新商品信息")
public class UpdateProductDto {

    @NonNull
    @Schema(description = "商品ID")
    private Long id;

    @NotEmpty
    @Schema(description = "商品名称")
    private String name;

    @Schema(description = "商品描述")
    private String description;
    
    @Schema(description = "商品价格")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT)
    @DecimalMin(value = "0.0", inclusive = false)
    private Float price;

    @Schema(description = "商品库存")
    private Integer stock;

    @Schema(description = "商品销量")
    private Integer sold;

    @NotEmpty
    @Schema(description = "商家名称")
    private String merchantName;

    @NotEmpty
    @Schema(description = "商品类别")
    private List<String> categories;

    @Schema(description = "状态（0上架，1下架）")
    private Integer status;
}
