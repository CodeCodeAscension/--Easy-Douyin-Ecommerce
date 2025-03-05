package com.example.payment.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.domain.ResponseResult;
import com.example.common.util.UserContextUtil;
import com.example.payment.domain.dto.*;
import com.example.payment.domain.vo.CreditVo;
import com.example.payment.service.CreditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 银行卡信息CRUD控制器
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/credits")
@Tag(name = "银行卡信息控制器", description = "银行卡信息控制器")
public class CreditController {

    @Resource
    private CreditService creditService;

    /**
     * 添加银行卡信息
     * @param dto 银行卡信息dto
     * @return ResponseResult对象
     */
    @PostMapping
    @Operation(summary = "添加银行卡信息")
    public ResponseResult<CreditVo> createCredit(@RequestBody @Validated CreditCreateDto dto) {
        Long userId = UserContextUtil.getUserId();
        return ResponseResult.success(creditService.createCredit(userId, dto));
    }

    /**
     * 删除银行卡信息
     * @param creditDto dto
     * @return ResponseResult对象
     */
    @DeleteMapping
    @Operation(summary = "删除银行卡信息")
    public ResponseResult<Object> deleteCredit(@RequestBody @Validated CreditGetDto creditDto) {
        creditService.deleteCredit(creditDto.getCardNumber());
        return ResponseResult.success();
    }


    /**
     * 获取银行卡信息
     * @param cardNumber 银行卡号
     * @return ResponseResult对象
     */
    @GetMapping("/{cardNumber}")
    @Operation(summary = "获取银行卡信息")
    public ResponseResult<CreditVo> getCreditById(@PathVariable("cardNumber") String cardNumber) {
        Long userId = UserContextUtil.getUserId();
        return ResponseResult.success(creditService.getCredit(userId, cardNumber));
    }

    /**
     * 获取某一个用户的所有银行卡信息
     * @param pageNum 页号
     * @param pageSize 页大小
     * @return 银行卡信息
     */
    @GetMapping
    @Operation(summary = "获取该用户的所有银行卡信息")
    public ResponseResult<IPage<CreditVo> > getCredits(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        Long userId = UserContextUtil.getUserId();
        return ResponseResult.success(creditService.getCreditListByUserId(userId, pageNum, pageSize));
    }
}
