package com.example.api.client.fallback;

import com.example.api.client.PaymentClient;
import com.example.api.domain.dto.payment.ChargeDto;
import com.example.api.domain.vo.payment.ChargeVo;
import com.example.common.domain.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentClientFallBack implements FallbackFactory<PaymentClient> {
    @Override
    public PaymentClient create(Throwable cause) {
        return new PaymentClient() {
            @Override
            public ResponseResult<ChargeVo> charge(ChargeDto chargeDto) {
                log.error("payment-service-exception:charge, "+cause.getMessage());
                return ResponseResult.errorFeign(cause);
            }

            @Override
            public ResponseResult<Object> cancelCharge(Integer transactionId) {
                log.error("payment-service-exception:cancelCharge, "+cause.getMessage());
                return ResponseResult.errorFeign(cause);
            }

            @Override
            public ResponseResult<Object> confirmCharge(String preTransactionId) {
                log.error("payment-service-exception:confirmCharge, "+cause.getMessage());
                return ResponseResult.errorFeign(cause);
            }
        };
    }
}
