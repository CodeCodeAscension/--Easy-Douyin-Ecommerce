package com.example.payment.controller.v1;

import com.example.api.domain.dto.payment.ChargeCancelDto;
import com.example.api.domain.dto.payment.ChargeDto;
import com.example.api.domain.vo.payment.ChargeVo;
import com.example.common.domain.ResponseResult;
import com.example.payment.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 支付处理控制器
 */
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "支付交易控制器", description = "支付交易控制器")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    @Operation(summary = "支付接口")
    public ResponseResult<ChargeVo> charge(@RequestBody @Validated ChargeDto chargeDto) {
        return ResponseResult.success(transactionService.charge(chargeDto));
    }

    @DeleteMapping
    @Operation(summary = "取消支付")
    public ResponseResult<Object> cancelCharge(@RequestParam String transactionId) {
        transactionService.cancelCharge(transactionId);
        return ResponseResult.success();
    }

    @PutMapping
    @Operation(summary = "设置定时支付")
    public ResponseResult<Object> autoCancelCharge(@RequestBody @Validated ChargeCancelDto chargeCancelDto) {
        transactionService.autoCancelCharge(chargeCancelDto);
        return ResponseResult.success();
    }

    @PostMapping("/confirm")
    @Operation(summary = "确认支付")
    public ResponseResult<Object> confirmCharge(@RequestParam String transactionId) {
        transactionService.confirmCharge(transactionId);
        return ResponseResult.success();
    }
}
