package com.example.product.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("product")
public class Product implements Serializable {

        private static final long serialVersionUID = 1L;

        @TableId(value = "id", type = IdType.ASSIGN_ID)
        private Long id;

        private String name;

        private String description;

        private Float price;

        // 销量
        private Integer sold;

        // 库存
        private Integer stoke;

        // 乐观锁
        private Integer version;

        // 商户名称
        private String merchantName;

        // 状态（0上架，1下架）
        private Integer status;

        private LocalDateTime createTime;

        private LocalDateTime updateTime;
}
