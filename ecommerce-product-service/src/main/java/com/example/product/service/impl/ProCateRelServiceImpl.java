package com.example.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.product.domain.po.ProCateRel;
import com.example.product.mapper.proCateRelMapper;
import com.example.product.service.IProCateRelService;
import org.springframework.stereotype.Service;

@Service
public class ProCateRelServiceImpl extends ServiceImpl<proCateRelMapper, ProCateRel> implements IProCateRelService {
}
