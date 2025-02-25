package com.example.product.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("product")
@Schema
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
        private Float price;

        // 销量
        @Schema(description = "销量")
        private Integer sold;

        // 库存
        @Schema(description = "库存")
        private Integer stoke;

        // 乐观锁
        @Version
        @Schema(description = "乐观锁")
        private Integer version;

        // 商户名称
        @Schema(description = "商户名称")
        private String merchantName;

        // 状态（0上架，1下架）
        @Schema(description = "状态（0上架，1下架）")
        private Integer status;

        // 创建时间
        @Schema(description = "创建时间")
        private LocalDateTime createTime;

        @Schema(description = "更新时间")
        private LocalDateTime updateTime;
}
