package com.example.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.product.domain.po.ProCateRel;
import com.example.product.mapper.proCateRelMapper;
import com.example.product.service.ICategoryService;
import com.example.product.service.IProCateRelService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProCateRelServiceImpl extends ServiceImpl<proCateRelMapper, ProCateRel> implements IProCateRelService {

}
