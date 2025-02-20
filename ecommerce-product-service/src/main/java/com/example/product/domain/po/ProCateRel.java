package com.example.product.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("product_category_relation")
public class ProCateRel implements Serializable {

        private static final long serialVersionUID = 1L;

        @TableId(value = "id", type = IdType.ASSIGN_ID)
        private Long id;

        private Long productId;

        private Long categoryId;

        private LocalDateTime createTime;

        private LocalDateTime updateTime;
}
