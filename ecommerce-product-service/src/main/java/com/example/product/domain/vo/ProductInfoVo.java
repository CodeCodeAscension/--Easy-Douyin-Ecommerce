package com.example.product.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class ProductInfoVo {

    private Long id;

    private String name;

    private String description;

    private Float price;

    private Integer sold;

    private Integer stoke;

    private String merchantName;

    private List<String> categories;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
