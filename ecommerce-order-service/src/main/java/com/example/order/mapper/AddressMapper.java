package com.example.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.api.domain.po.Address;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户收货地址信息 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2025-02-28
 */
@Mapper
public interface AddressMapper extends BaseMapper<Address> {

}
