package com.example.checkout.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.api.client.OrderClient;
import com.example.api.client.PaymentClient;
import com.example.api.domain.dto.order.PlaceOrderDto;
import com.example.api.domain.dto.payment.ChargeDto;
import com.example.api.domain.dto.payment.TransactionInfoDto;
import com.example.api.domain.po.CartItem;
import com.example.api.domain.vo.order.PlaceOrderVo;
import com.example.api.domain.vo.payment.ChargeVo;
import com.example.api.domain.vo.payment.TransactionInfoVo;
import com.example.checkout.domain.dto.CheckoutDto;
import com.example.checkout.domain.po.CheckoutPo;
import com.example.checkout.domain.vo.CheckoutVo;
import com.example.checkout.mapper.CheckoutMapper;
import com.example.checkout.service.CheckoutService;
import com.example.common.domain.ResponseResult;
import com.example.common.exception.SystemException;
import com.example.common.util.UserContextUtil;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class CheckoutServiceImpl extends ServiceImpl<CheckoutMapper, CheckoutPo> implements CheckoutService {

    @Resource
    private OrderClient orderClient;

    @Resource
    private PaymentClient paymentClient;
    /**
     * 订单结算
     * 自动免密支付
     *
     * @param checkoutDto 结算信息
     */
    @GlobalTransactional(name = "Checkout", rollbackFor = Exception.class)
    @Override
    public ResponseResult<CheckoutVo> checkout(CheckoutDto checkoutDto) {
        Long userId = UserContextUtil.getUserId();
        Assert.notNull(userId, "用户未登录");
        try {

            Long cartId = checkoutDto.getCartId();
            PlaceOrderDto placeOrderDto = checkoutDto.getPlaceOrderDto();

            // 创建订单
            ResponseResult<PlaceOrderVo> responseResult = orderClient.placeOrder(placeOrderDto);
            if (responseResult.getCode() != 200) {
                throw new SystemException(new RuntimeException("创建订单失败"));
            }
            PlaceOrderVo placeOrderVo = responseResult.getData();

            // 获取订单id
            String orderId = placeOrderVo.getOrder().getOrderId();

            ChargeDto chargeDto = new ChargeDto();
            chargeDto.setOrderId(orderId);
            chargeDto.setCreditId(checkoutDto.getCreditId());


            // 发起支付请求
            ResponseResult<ChargeVo> charge = paymentClient.charge(chargeDto);
            if (charge.getCode() != 200) {
                throw new SystemException(new RuntimeException("支付请求失败"));
            }

            // 确认支付
            ChargeVo chargeVo = charge.getData();
            ResponseResult<Object> result =paymentClient.confirmCharge(chargeVo.getTransactionId());
            if (result.getCode() != 200) {
                throw new SystemException(new RuntimeException("支付确认失败"));
            }

            // 获取交易信息
            TransactionInfoDto transactionInfoDto = new TransactionInfoDto();
            transactionInfoDto.setTransactionId(chargeVo.getTransactionId());
            transactionInfoDto.setPreTransactionId(chargeVo.getPreTransactionId());

            ResponseResult<TransactionInfoVo> transactionInfo = paymentClient.getTransactionInfo(transactionInfoDto);
            if (transactionInfo.getCode() != 200) {
                throw new SystemException(new RuntimeException("获取交易信息失败"));
            }
            TransactionInfoVo transactionInfoVo = transactionInfo.getData();

            // 保存结算信息
            CheckoutPo checkoutPo = CheckoutPo.builder()
                    .userId(userId)
                    .cartId(cartId)
                    .orderId(orderId)
                    .transactionId(chargeVo.getTransactionId())
                    .status(transactionInfoVo.getStatus().getCode())
                    .reason(transactionInfoVo.getReason() == null ? null : transactionInfoVo.getReason())
                    .firstname(checkoutDto.getFirstname() == null ? null : checkoutDto.getFirstname())
                    .lastname(checkoutDto.getLastname() == null ? null : checkoutDto.getLastname())
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build();

            if (!save(checkoutPo)) {
                throw new SystemException(new RuntimeException("保存结算信息失败"));
            }

            // 返回结算信息
            CheckoutVo checkoutVo = CheckoutVo.builder()
                    .orderId(orderId)
                    .transactionId(chargeVo.getTransactionId())
                    .build();

            return ResponseResult.success(checkoutVo);
        }catch (Exception e) {
            log.error("结算服务异常：", e);
            throw new SystemException("结算失败， 请稍后尝试", e);
        }
    }
}
