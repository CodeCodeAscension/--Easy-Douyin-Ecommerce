package com.example.payment.controller.v1;

import com.example.api.domain.dto.payment.ChargeDto;
import com.example.api.domain.vo.payment.ChargeVo;
import com.example.common.domain.ResponseResult;
import com.example.payment.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/inner/payments")
@Tag(name="内部方法", description = "内部方法")
public class TransactionInnerController {
    private final TransactionService transactionService;

    @PostMapping
    @Operation(summary = "支付接口")
    public ResponseResult<ChargeVo> chargeInner(@RequestBody @Validated ChargeDto chargeDto) {
        return ResponseResult.success(transactionService.quickCharge(chargeDto));
    }
}
