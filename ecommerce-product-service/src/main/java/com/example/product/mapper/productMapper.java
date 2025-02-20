package com.example.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.product.domain.po.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface productMapper extends BaseMapper<Product> {
}
