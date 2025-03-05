package com.example.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.cache.MybatisRedisCache;
import com.example.payment.domain.po.Credit;
import com.example.payment.domain.vo.CreditVo;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
@CacheNamespace(implementation = MybatisRedisCache.class)
public interface CreditMapper extends BaseMapper<Credit> {

    @Select("select * from db_payment.credit where user_id = #{userId} and deleted = 0")
    IPage<CreditVo> selectToVo(IPage<CreditVo> page, Long userId);
}
