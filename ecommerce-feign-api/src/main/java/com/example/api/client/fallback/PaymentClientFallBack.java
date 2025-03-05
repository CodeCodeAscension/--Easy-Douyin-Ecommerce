package com.example.api.client.fallback;

import com.example.api.client.PaymentClient;
import com.example.common.domain.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentClientFallBack implements FallbackFactory<PaymentClient> {
    @Override
    public PaymentClient create(Throwable cause) {
        return chargeDto -> {
            log.error("payment-service-exception:charge, {}", cause.getMessage());
            return ResponseResult.errorFeign(cause);
        };
    }
}
