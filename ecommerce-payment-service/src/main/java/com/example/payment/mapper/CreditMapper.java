package com.example.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.payment.domain.po.Credit;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CreditMapper extends BaseMapper<Credit> {
}
