package com.example.payment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.domain.ResponseResult;
import com.example.payment.domain.dto.ChargeCancelDto;
import com.example.payment.domain.dto.ChargeDto;
import com.example.payment.domain.dto.CreditDto;
import com.example.payment.domain.dto.CreditUpdateDto;
import com.example.payment.domain.po.Credit;
import com.example.payment.domain.vo.ChargeVo;
import com.example.payment.domain.vo.CreditVo;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface CreditService extends IService<Credit> {

    /**
     * 支付功能
     *
     * @param chargeDto 订单支付信息
     * @rerurn chargeVo 支付结果
     */
    ResponseResult<ChargeVo> charge(ChargeDto chargeDto);

    /**
     * 取消支付
     * @param transactionId
     * @return
     */
    ResponseResult<Object> cancelCharge(String transactionId);

    /**
     * 定期取消支付
     * @param chargeCancelDto
     * @return
     */
    ResponseResult<Object> autoCancelCharge(ChargeCancelDto chargeCancelDto);

    /**
     * 输入信用卡信息
     */
    ResponseResult<CreditVo> createCredit(CreditDto creditDto);

    /**
     * 删除信用卡信息
     */
    ResponseResult<Object> deleteCredit(String cardNumber);

    /**
     * 更新信用卡信息
     */
    ResponseResult<CreditVo> updateCredit(CreditUpdateDto creditUpdateDto);

    /**
     * 查询信用卡信息
     */
    ResponseResult<CreditVo> getCredit(String cardNumber);

    /**
     * 确认支付
     */
    ResponseResult<Object> confirmCharge(String transactionId);

}
