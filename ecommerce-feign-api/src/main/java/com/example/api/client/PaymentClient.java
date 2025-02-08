package com.example.api.client;

import com.example.api.domain.dto.payment.ChargeDto;
import com.example.api.domain.vo.payment.ChargeVo;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("payment-service")
public interface PaymentClient {
    ChargeVo charge(ChargeDto chargeDto);
}
