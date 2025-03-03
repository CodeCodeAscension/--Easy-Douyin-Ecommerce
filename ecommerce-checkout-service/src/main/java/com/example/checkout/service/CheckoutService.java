package com.example.checkout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.checkout.domain.dto.CheckoutDto;
import com.example.checkout.domain.po.CheckoutPo;
import com.example.checkout.domain.vo.CheckoutVo;

public interface CheckoutService extends IService<CheckoutPo> {

    /**
     * 订单结算
     * 自动免密支付
     * @param userId 用户ID
     * @param checkoutDto 结算信息
     */
    CheckoutVo checkout(Long userId, CheckoutDto checkoutDto);

}
