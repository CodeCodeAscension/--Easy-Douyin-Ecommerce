package com.example.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.api.domain.vo.order.AddressInfoVo;
import com.example.common.exception.DatabaseException;
import com.example.common.exception.NotFoundException;
import com.example.order.domain.dto.AddressDto;
import com.example.order.domain.dto.AddressUpdateDto;
import com.example.order.domain.po.Address;

/**
 * <p>
 * 用户收货地址信息 服务类
 * </p>
 *
 * @author author
 * @since 2025-02-28
 */
public interface IAddressService extends IService<Address> {

    /**
     * 创建新的地址记录
     * @param userId 用户ID
     * @param addressDto dto
     * @throws DatabaseException 数据库异常
     */
    void addAddress(Long userId, AddressDto addressDto) throws DatabaseException;

    /**
     * 获取地址记录信息
     * @param userId 用户ID
     * @param id 地址ID
     * @return vo
     * @throws DatabaseException 数据库异常
     * @throws NotFoundException 未找到
     */
    AddressInfoVo getAddressInfo(Long userId, Long id) throws DatabaseException, NotFoundException;

    /**
     * 删除地址记录
     * @param userId 用户ID
     * @param id 地址ID
     * @throws NotFoundException 未找到
     */
    void deleteAddress(Long userId, Long id) throws NotFoundException;

    /**
     * 更新地址记录
     * @param userId 用户ID
     * @param addressUpdateDto dto
     * @throws DatabaseException 数据库异常
     * @throws NotFoundException 未找到
     */
    void updateAddress(Long userId, AddressUpdateDto addressUpdateDto) throws DatabaseException, NotFoundException;

    /**
     * 获得某个用户的所有地址信息
     * @param userId 用户ID
     * @param pageNum 页号
     * @param pageSize 页大小
     * @return 信息
     */
    IPage<AddressInfoVo> getAddressesByUserId(Long userId, Integer pageNum, Integer pageSize);
}
