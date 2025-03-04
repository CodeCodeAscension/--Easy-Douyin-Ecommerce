package com.example.api.domain.vo.product;

import com.example.api.enums.ProductStatusEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "商品信息显示")
public class ProductInfoVo {
    @Schema(description = "商品ID")
    private Long id;
    @Schema(description = "商品名称")
    private String name;
    @Schema(description = "商品描述")
    private String description;
    @Schema(description = "商品价格")
    private Float price;
    @Schema(description = "商品销量")
    private Integer sold;
    @Schema(description = "商品库存")
    private Integer stock;
    @Schema(description = "商家名称")
    private String merchantName;
    @Schema(description = "所属类别")
    private List<String> categories;
    @Schema(description = "状态（0上架，1下架）")
    private ProductStatusEnum status;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "修改时间")
    private LocalDateTime updateTime;
}
