package com.example.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.payment.domain.po.Transaction;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TransactionMapper extends BaseMapper<Transaction> {
}
