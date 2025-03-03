package com.example.api.client;

import com.example.api.client.fallback.PaymentClientFallBack;
import com.example.api.domain.dto.payment.ChargeDto;
import com.example.api.domain.dto.payment.TransactionInfoDto;
import com.example.api.domain.vo.payment.ChargeVo;
import com.example.api.domain.vo.payment.TransactionInfoVo;
import com.example.common.domain.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "payment-service", fallbackFactory = PaymentClientFallBack.class)
public interface PaymentClient {
    // 订单支付
    @PostMapping("/api/v1/payments")
    ResponseResult<ChargeVo> charge(@RequestBody ChargeDto chargeDto);

    // 取消支付
    @DeleteMapping("/api/v1/payments")
    ResponseResult<Object> cancelCharge(@RequestParam Integer transactionId);

    // 确认支付
    @PostMapping("/confirm")
    ResponseResult<Object> confirmCharge(@RequestParam String preTransactionId);

    @GetMapping("/api/v1/payments/byId")
    ResponseResult<TransactionInfoVo> getTransactionInfo(@RequestBody TransactionInfoDto transactionInfoDto);
}
