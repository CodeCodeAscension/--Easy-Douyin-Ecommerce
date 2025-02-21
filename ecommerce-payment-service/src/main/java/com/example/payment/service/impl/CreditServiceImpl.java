package com.example.payment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.domain.ResponseResult;
import com.example.common.util.UserContextUtil;
import com.example.payment.domain.dto.ChargeDto;
import com.example.payment.domain.po.Credit;
import com.example.payment.domain.vo.ChargeVo;
import com.example.payment.enums.PaymentStatusEnum;
import com.example.payment.mapper.CreditMapper;
import com.example.payment.service.CreditService;
import com.example.payment.service.TransactionService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditServiceImpl extends ServiceImpl<CreditMapper, Credit> implements CreditService {

    @Resource
    private CreditMapper creditMapper;

    @Resource
    private TransactionService transactionService;

    /**
     * 支付功能
     *
     * @param chargeDto 订单支付信息
     * @rerurn chargeVo 支付结果
     */
    @Transactional
    @Override
    public ResponseResult<ChargeVo> pay(ChargeDto chargeDto) {
        // 判断用户是否登录
        Long userId = UserContextUtil.getUserId();
        if (userId == null) {
            log.error("用户未登录");
            return ResponseResult.error(PaymentStatusEnum.USER_NOT_LOGIN.getErrorCode(), PaymentStatusEnum.USER_NOT_LOGIN.getErrorMessage());
        }

        // 获取订单id
        String orderId = chargeDto.getOrderId();
        if (orderId == null) {
            log.error("订单id为空");
            return ResponseResult.error(PaymentStatusEnum.ORDER_ID_IS_NULL.getErrorCode(), PaymentStatusEnum.ORDER_ID_IS_NULL.getErrorMessage());
        }

        // TODO 查询订单是否存在

        return null;
    }

}
