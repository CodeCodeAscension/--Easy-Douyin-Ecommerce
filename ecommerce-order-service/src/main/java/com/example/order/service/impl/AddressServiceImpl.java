package com.example.order.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.api.domain.vo.order.AddressInfoVo;
import com.example.common.exception.DatabaseException;
import com.example.common.exception.NotFoundException;
import com.example.order.domain.dto.AddressDto;
import com.example.order.domain.dto.AddressUpdateDto;
import com.example.order.domain.po.Address;
import com.example.order.mapper.AddressMapper;
import com.example.order.service.IAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 用户收货地址信息 服务实现类
 * </p>
 *
 * @author author
 * @since 2025-02-28
 */
@Service
@AllArgsConstructor
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {

    private final AddressMapper addressMapper;

    @Override
    public void addAddress(Long userId, AddressDto addressDto) throws DatabaseException {
        Address address = Address.builder()
                .userId(userId)
                .streetAddress(addressDto.getStreetAddress())
                .city(addressDto.getCity())
                .country(addressDto.getCountry())
                .zipCode(addressDto.getZipCode())
                .province(addressDto.getProvince())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        if(!this.save(address)) {
            throw new DatabaseException("地址新增失败");
        }
    }

    @Override
    public AddressInfoVo getAddressInfo(Long userId, Long id) throws DatabaseException, NotFoundException {
        Address address = this.getById(id);
        if(address == null || !address.getUserId().equals(userId)) {
            throw new NotFoundException("未找到记录");
        }
        AddressInfoVo addressInfoVo = new AddressInfoVo();
        BeanUtils.copyProperties(address, addressInfoVo);
        return addressInfoVo;
    }

    @Override
    public void deleteAddress(Long userId, Long id) throws NotFoundException {
        if(!this.remove(Wrappers.<Address>lambdaQuery()
                .eq(Address::getUserId, userId)
                .eq(Address::getId, id))) {
            throw new NotFoundException("不存在这条记录");
        }
    }

    @Override
    public void updateAddress(Long userId, AddressUpdateDto addressUpdateDto) throws DatabaseException, NotFoundException {
        Address address = this.getById(addressUpdateDto.getId());
        if(address == null || !Objects.equals(address.getUserId(), userId)) {
            throw new NotFoundException("不存在这条记录");
        }
        BeanUtils.copyProperties(addressUpdateDto, address);
        address.setUpdateTime(LocalDateTime.now());
        if(!this.updateById(address)) {
            throw new DatabaseException("地址更新失败");
        }
    }

    @Override
    public IPage<AddressInfoVo> getAddressesByUserId(Long userId, Integer pageNum, Integer pageSize) {
        IPage<AddressInfoVo> page = new Page<>(pageNum, pageSize);
        return addressMapper.getAddressByUserId(page, userId);
    }
}
