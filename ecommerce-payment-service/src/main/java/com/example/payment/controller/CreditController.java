package com.example.payment.controller;

import com.example.common.domain.ResponseResult;
import com.example.payment.domain.dto.ChargeDto;
import com.example.payment.domain.vo.ChargeVo;
import com.example.payment.service.CreditService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/payments")
public class CreditController {

    @Resource
    private CreditService creditService;

    @PostMapping
    public ResponseResult<ChargeVo> charge(@RequestBody ChargeDto chargeDto) {
        return creditService.charge(chargeDto);
    }

    @DeleteMapping
    public ResponseResult<Object> cancelCharge(@RequestParam String transactionId) {
        return creditService.cancelCharge(transactionId);
    }
}
