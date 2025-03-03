package com.example.checkout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.api.client.OrderClient;
import com.example.api.client.PaymentClient;
import com.example.api.domain.dto.order.PlaceOrderDto;
import com.example.api.domain.dto.payment.ChargeDto;
import com.example.api.domain.dto.payment.TransactionInfoDto;
import com.example.api.domain.vo.order.PlaceOrderVo;
import com.example.api.domain.vo.payment.ChargeVo;
import com.example.api.domain.vo.payment.TransactionInfoVo;
import com.example.checkout.domain.dto.CheckoutDto;
import com.example.checkout.domain.po.CheckoutPo;
import com.example.checkout.domain.vo.CheckoutVo;
import com.example.checkout.mapper.CheckoutMapper;
import com.example.checkout.service.CheckoutService;
import com.example.common.domain.ResponseResult;
import com.example.common.domain.ResultCode;
import com.example.common.exception.DatabaseException;
import com.example.common.exception.SystemException;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class CheckoutServiceImpl extends ServiceImpl<CheckoutMapper, CheckoutPo> implements CheckoutService {

    @Resource
    private OrderClient orderClient;

    @Resource
    private PaymentClient paymentClient;

    @GlobalTransactional(name = "Checkout", rollbackFor = Exception.class)
    @Override
    public CheckoutVo checkout(Long userId, CheckoutDto checkoutDto) {
        try {

            Long cartId = checkoutDto.getCartId();
            PlaceOrderDto placeOrderDto = checkoutDto.getPlaceOrderDto();

            // 创建订单
            ResponseResult<PlaceOrderVo> responseResult = orderClient.placeOrder(placeOrderDto);
            if (responseResult.getCode() != ResultCode.SUCCESS) {
                throw new SystemException("创建订单失败："+responseResult.getMsg());
            }
            PlaceOrderVo placeOrderVo = responseResult.getData();

            // 获取订单id
            String orderId = placeOrderVo.getOrder().getOrderId();

            ChargeDto chargeDto = new ChargeDto();
            chargeDto.setOrderId(orderId);
            chargeDto.setCreditId(checkoutDto.getCreditId());


            // 发起支付请求
            ResponseResult<ChargeVo> charge = paymentClient.charge(chargeDto);
            if (charge.getCode() != ResultCode.SUCCESS) {
                throw new SystemException("支付请求失败："+charge.getMsg());
            }

            // 确认支付
            ChargeVo chargeVo = charge.getData();
            ResponseResult<Object> result = paymentClient.confirmCharge(chargeVo.getTransactionId());
            if (result.getCode() != ResultCode.SUCCESS) {
                throw new SystemException("支付确认失败："+result.getMsg());
            }

            // 获取交易信息
            TransactionInfoDto transactionInfoDto = new TransactionInfoDto();
            transactionInfoDto.setTransactionId(chargeVo.getTransactionId());
            transactionInfoDto.setPreTransactionId(chargeVo.getPreTransactionId());

            ResponseResult<TransactionInfoVo> transactionInfo = paymentClient.getTransactionInfo(transactionInfoDto);
            if (transactionInfo.getCode() != ResultCode.SUCCESS) {
                throw new SystemException("获取交易信息失败："+transactionInfo.getMsg());
            }
            TransactionInfoVo transactionInfoVo = transactionInfo.getData();

            // 保存结算信息
            CheckoutPo checkoutPo = CheckoutPo.builder()
                    .userId(userId)
                    .cartId(cartId)
                    .orderId(orderId)
                    .transactionId(chargeVo.getTransactionId())
                    .status(transactionInfoVo.getStatus().getCode())
                    .reason(transactionInfoVo.getReason())
                    .firstname(checkoutDto.getFirstname())
                    .lastname(checkoutDto.getLastname())
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build();

            if (!save(checkoutPo)) {
                throw new DatabaseException("保存结算信息失败");
            }

            // 返回结算信息

            return CheckoutVo.builder()
                    .orderId(orderId)
                    .transactionId(chargeVo.getTransactionId())
                    .build();
        }catch (Exception e) {
            log.error("结算服务异常：", e);
            throw new SystemException("结算失败， 请稍后尝试", e);
        }
    }
}
