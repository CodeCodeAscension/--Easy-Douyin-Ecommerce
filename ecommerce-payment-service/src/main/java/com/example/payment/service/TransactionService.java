package com.example.payment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.api.domain.dto.payment.ChargeCancelDto;
import com.example.api.domain.dto.payment.ChargeDto;
import com.example.api.domain.vo.payment.ChargeVo;
import com.example.common.domain.ResponseResult;
import com.example.common.exception.SystemException;
import com.example.common.exception.UserException;
import com.example.payment.domain.po.Transaction;

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
     * 取消支付
     * @param transactionId 交易ID
     * @throws UserException 用户异常
     * @throws SystemException 系统异常
     */
    void cancelCharge(String transactionId) throws UserException, SystemException;

    /**
     * 定期取消支付
     * @param chargeCancelDto dto
     * @throws UserException 用户异常
     * @throws SystemException 系统异常
     */
    void autoCancelCharge(ChargeCancelDto chargeCancelDto);

    /**
     * 确认支付
     * @param transactionId 交易ID
     * @throws UserException 用户异常
     * @throws SystemException 系统异常
     */
    void confirmCharge(String transactionId);
}
