package com.example.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.api.domain.vo.order.AddressInfoVo;
import com.example.common.cache.MybatisRedisCache;
import com.example.order.domain.po.Address;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 用户收货地址信息 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2025-02-28
 */
@Mapper
@CacheNamespace(implementation = MybatisRedisCache.class)
public interface AddressMapper extends BaseMapper<Address> {

    @Select("select * from db_order.address where user_id = #{userId}")
    IPage<AddressInfoVo> getAddressByUserId(IPage<AddressInfoVo> page, Long userId);
}
