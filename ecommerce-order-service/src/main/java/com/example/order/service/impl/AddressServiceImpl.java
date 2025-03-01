package com.example.order.service.impl;

import com.example.api.domain.po.Address;
import com.example.order.mapper.AddressMapper;
import com.example.order.service.IAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户收货地址信息 服务实现类
 * </p>
 *
 * @author author
 * @since 2025-02-28
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {

}
