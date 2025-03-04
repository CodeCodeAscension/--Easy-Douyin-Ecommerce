package com.example.product.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import com.example.api.enums.ProductStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("product")
@Schema(description = "商品实体类")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "商品ID")
    private Long id;

    @Schema(description = "商品名称")
    private String name;

    @Schema(description = "商品描述")
    private String description;

    @Schema(description = "商品价格")
    @DecimalMin(value = "0.00", message = "价格必须大于0")
    private Float price;

    @Min(value = 0, message = "销量必须大于或等于0")
    @Schema(description = "销量")
    private Integer sold;

    @Min(value = 0, message = "库存必须大于或等于0")
    @Schema(description = "库存")
    private Integer stock;

    @Version
    @Schema(description = "乐观锁")
    private Integer version;

    @Schema(description = "商户名称")
    private String merchantName;

    @Schema(description = "状态（0上架，1下架）")
    private ProductStatusEnum status;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
