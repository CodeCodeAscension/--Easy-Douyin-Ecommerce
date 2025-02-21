package com.example.payment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.domain.ResponseResult;
import com.example.payment.domain.dto.ChargeDto;
import com.example.payment.domain.po.Credit;
import com.example.payment.domain.vo.ChargeVo;

public interface CreditService extends IService<Credit> {

    /**
     * 支付功能
     *
     * @param chargeDto 订单支付信息
     * @rerurn chargeVo 支付结果
     */
    public ResponseResult<ChargeVo> pay(ChargeDto chargeDto);
}
