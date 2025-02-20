package com.example.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.product.domain.po.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface categoryMapper extends BaseMapper<Category> {
}
