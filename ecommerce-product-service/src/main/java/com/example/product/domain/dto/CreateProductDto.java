package com.example.product.domain.dto;

import com.example.api.enums.ProductStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "创建商品DTO")
public class CreateProductDto {

    @NotEmpty
    @Schema(description = "商品名称")
    private String name;

    @Schema(description = "商品描述")
    @NotEmpty
    private String description;

    @NotNull
    @Schema(description = "商品价格")
    @DecimalMin(value = "0.0", inclusive = false)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT)
    private Float price;

    @Schema(description = "商品库存")
    @Min(0)
    private Integer stock = 0;

    @NotEmpty
    @Schema(description = "商家名称")
    private String merchantName;

    @NotEmpty
    @Schema(description = "商品类别")
    private List<String> categories;

    @Schema(description = "状态（0上架，1下架）")
    private ProductStatusEnum status = ProductStatusEnum.PUT_ON;
}
