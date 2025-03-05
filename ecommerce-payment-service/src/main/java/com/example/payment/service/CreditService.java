package com.example.payment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.exception.SystemException;
import com.example.common.exception.UserException;
import com.example.payment.domain.dto.CreditCreateDto;
import com.example.payment.domain.dto.CreditDto;
import com.example.payment.domain.dto.CreditUpdateDto;
import com.example.payment.domain.po.Credit;
import com.example.payment.domain.vo.CreditVo;

public interface CreditService extends IService<Credit> {

    /**
     * 创建信用卡信息
     * @param creditCreateDto dto
     * @throws UserException 用户异常
     * @throws SystemException 系统异常
     * @return CreditVo对象
     */
    CreditVo createCredit(Long userId, CreditCreateDto creditCreateDto) throws UserException, SystemException;

    /**
     * 删除信用卡信息
     * @param cardNumber 信用卡卡号
     * @throws UserException 用户异常
     * @throws SystemException 系统异常
     */
    void deleteCredit(String cardNumber) throws UserException, SystemException;

    /**
     * 更新信用卡信息
     * @param creditUpdateDto dto
     * @throws UserException 用户异常
     * @throws SystemException 系统异常
     * @return CreditVo对象
     */
    CreditVo updateCredit(CreditUpdateDto creditUpdateDto) throws UserException, SystemException;

    /**
     * 查询信用卡信息
     * @param cardNumber 信用卡ID
     * @param userId 用户ID
     * @throws UserException 用户异常
     * @throws SystemException 系统异常
     * @return CreditVo对象
     */
    CreditVo getCredit(Long userId, String cardNumber) throws UserException, SystemException;

    /**
     * 检查银行卡是否可用
     * @param userId 用户ID
     * @param cardNumber 银行卡号
     * @throws UserException 用户异常
     * @throws SystemException 系统异常
     * @return Credit对象
     */
    Credit checkCreditPermission(Long userId, String cardNumber) throws UserException, SystemException;


    /**
     * 支付扣款，失败则抛出异常
     * @param userId 用户ID
     * @param cardNumber 银行卡号
     * @param amount 金额
     * @throws UserException 用户异常
     * @throws SystemException 系统异常
     */
    void pay(Long userId, String cardNumber, Float amount) throws UserException, SystemException;

    /**
     * 获得某个用户的银行卡所有信息
     * @param userId 用户ID
     * @param pageNum 页数
     * @param pageSize 页大小
     * @return IPage(CreditVo)对象
     * @throws UserException 用户异常
     * @throws SystemException 系统异常
     */
    IPage<CreditVo> getCreditListByUserId(Long userId, Integer pageNum, Integer pageSize) throws UserException, SystemException;
}
