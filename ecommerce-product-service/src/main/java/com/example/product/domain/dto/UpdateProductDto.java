package com.example.product.domain.dto;

import com.example.api.enums.ProductStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "更新商品信息")
public class UpdateProductDto {

    @NotNull
    @Schema(description = "商品ID")
    private Long id;

    @Schema(description = "商品名称")
    private String name;

    @Schema(description = "商品描述")
    private String description;

    @Schema(description = "商品价格")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT)
    @DecimalMin(value = "0.0", inclusive = false)
    private Float price;

    @Schema(description = "商品库存")
    @Min(0)
    private Integer stock;

    @Schema(description = "商品销量")
    @Min(0)
    private Integer sold;

    @Schema(description = "商家名称")
    private String merchantName;

    @Schema(description = "商品类别")
    private List<String> categories;

    @Schema(description = "状态（0上架，1下架）")
    private ProductStatusEnum status;
}
