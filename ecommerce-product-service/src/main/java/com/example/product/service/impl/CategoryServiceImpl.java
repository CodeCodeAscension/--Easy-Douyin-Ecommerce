package com.example.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.product.domain.po.Category;
import com.example.product.mapper.categoryMapper;
import com.example.product.service.ICategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<categoryMapper, Category> implements ICategoryService {
}
