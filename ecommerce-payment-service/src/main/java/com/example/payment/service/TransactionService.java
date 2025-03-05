package com.example.payment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.api.domain.dto.payment.ChargeDto;
import com.example.api.domain.vo.payment.ChargeVo;
import com.example.common.exception.SystemException;
import com.example.common.exception.UserException;
import com.example.payment.domain.po.Transaction;
import com.example.api.domain.vo.payment.TransactionInfoVo;
import io.seata.spring.annotation.GlobalTransactional;

public interface TransactionService extends IService<Transaction> {

    /**
     * 支付功能
     * @param chargeDto 订单支付dto
     * @throws UserException 用户异常
     * @throws SystemException 系统异常
     * @return chargeVo 支付结果
     */
    ChargeVo charge(ChargeDto chargeDto) throws UserException, SystemException;

    /**
     * 支付功能（快捷自动确认功能）
     * @param chargeDto 订单支付dto
     * @throws UserException 用户异常
     * @throws SystemException 系统异常
     * @return chargeVo 支付结果
     */
    ChargeVo quickCharge(ChargeDto chargeDto) throws UserException, SystemException;

    /**
     * 取消支付
     * @param preTransactionId 预交易ID
     * @throws UserException 用户异常
     * @throws SystemException 系统异常
     */
    void cancelCharge(String preTransactionId) throws UserException, SystemException;

    /**
     * 取消支付（内部方法）
     * @param userId 用户ID
     * @param preTransactionId 预交易ID
     * @throws UserException 用户异常
     * @throws SystemException 系统异常
     */
     void cancelCharge(Long userId, String preTransactionId) throws UserException, SystemException;

    /**
     * 确认支付
     * @param preTransactionId 交易ID
     * @throws UserException 用户异常
     * @throws SystemException 系统异常
     */
    void confirmCharge(String preTransactionId) throws UserException, SystemException;

    /**
     * 获取交易信息
     * @param userId 用户ID
     * @param transactionId 交易ID
     * @param preTransactionId 预交易ID
     * @throws UserException 用户异常
     * @throws SystemException 系统异常
     * @return 交易信息
     */
    TransactionInfoVo getTransaction(Long userId, String transactionId, String preTransactionId) throws UserException, SystemException;

    /**
     * 获取某个用户的所有交易信息
     * @param pageNum 页号
     * @param pageSize 页数
     * @param userId 用户ID
     * @return IPage TransactionInfoVo 对象
     * @throws UserException 用户异常
     * @throws SystemException 系统异常
     */
    IPage<TransactionInfoVo> getTransactionInfos(Integer pageNum, Integer pageSize, Long userId) throws UserException, SystemException;
}
