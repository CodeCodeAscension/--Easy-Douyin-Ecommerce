package com.example.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.cache.MybatisRedisCache;
import com.example.payment.domain.po.Transaction;
import com.example.api.domain.vo.payment.TransactionInfoVo;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
@CacheNamespace(implementation = MybatisRedisCache.class)
public interface TransactionMapper extends BaseMapper<Transaction> {

    /**
     * 根据交易ID或者预交易ID查询交易对象（SQL语句在Mapper.xml里）
     * @param userId 用户ID
     * @param transactionId 交易ID
     * @param preTransactionId 预交易ID
     * @return 交易对象
     */
    Transaction findByIdAndPreId(Long userId, String transactionId, String preTransactionId);

    @Select("select * from db_payment.transaction where user_id = #{userId}")
    IPage<TransactionInfoVo> findByUserId(IPage<TransactionInfoVo> page, Long userId);
}
