package com.example.payment.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.api.domain.dto.payment.ChargeDto;
import com.example.api.domain.vo.payment.ChargeVo;
import com.example.common.domain.ResponseResult;
import com.example.common.util.UserContextUtil;
import com.example.api.domain.dto.payment.TransactionInfoDto;
import com.example.api.domain.vo.payment.TransactionInfoVo;
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

    @GetMapping("/byId")
    @Operation(summary = "获取支付信息")
    public ResponseResult<TransactionInfoVo> getTransactionInfo(@RequestBody TransactionInfoDto transactionInfoDto) {
        Long userId = UserContextUtil.getUserId();
        String transactionId = transactionInfoDto.getTransactionId();
        String preTransactionId = transactionInfoDto.getPreTransactionId();
        return ResponseResult.success(transactionService.getTransaction(userId, transactionId, preTransactionId));
    }

    @GetMapping
    @Operation(summary = "获取用户所有支付信息")
    public ResponseResult<IPage<TransactionInfoVo>> getTransactionInfos(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize) {
        Long userId = UserContextUtil.getUserId();
        return ResponseResult.success(transactionService.getTransactionInfos(pageNum, pageSize,userId));
    }

    @PostMapping
    @Operation(summary = "支付接口")
    public ResponseResult<ChargeVo> charge(@RequestBody @Validated ChargeDto chargeDto) {
        return ResponseResult.success(transactionService.charge(chargeDto));
    }

    @DeleteMapping
    @Operation(summary = "取消支付")
    public ResponseResult<Object> cancelCharge(@RequestParam String preTransactionId) {
        transactionService.cancelCharge(preTransactionId);
        return ResponseResult.success();
    }

    @PostMapping("/confirm")
    @Operation(summary = "确认支付")
    public ResponseResult<Object> confirmCharge(@RequestParam String preTransactionId) {
        transactionService.confirmCharge(preTransactionId);
        return ResponseResult.success();
    }
}
