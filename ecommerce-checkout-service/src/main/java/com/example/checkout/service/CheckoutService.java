package com.example.checkout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.checkout.domain.dto.CheckoutDto;
import com.example.checkout.domain.po.CheckoutPo;
import com.example.checkout.domain.vo.CheckoutVo;
import com.example.common.domain.ResponseResult;

public interface CheckoutService extends IService<CheckoutPo> {

    /**
     * 订单结算
     * 自动免密支付
     *
     * @param CheckoutDto 结算信息
     */
    ResponseResult<CheckoutVo> checkout(CheckoutDto checkoutDto);

}
