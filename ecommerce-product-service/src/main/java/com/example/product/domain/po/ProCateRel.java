package com.example.product.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("product_category_relation")
@Schema(description = "商品分类关联实体类")
@NoArgsConstructor
public class ProCateRel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "商品分类关联ID")
    private Long id;

    @Schema(description = "商品ID")
    private Long productId;

    @Schema(description = "分类ID")
    private Long categoryId;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    public ProCateRel(Long productId, Long cid) {
        this.productId = productId;
        this.categoryId = cid;
    }
}
