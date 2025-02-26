package com.example.payment.controller.v1;

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
     * @param creditDto 银行卡dto
     * @return ResponseResult对象
     */
    @PostMapping
    @Operation(summary = "添加银行卡信息")
    public ResponseResult<CreditVo> createCredit(@RequestBody @Validated CreditDto creditDto) {
        Long userId = UserContextUtil.getUserId();
        return ResponseResult.success(creditService.createCredit(userId, creditDto));
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

//    /**
//     * 更新银行卡信息（管理员接口）
//     * @param creditUpdateDto dto
//     * @return ResponseResult对象
//     */
//    @Operation(summary = "更新银行卡信息")
//    @PutMapping
//    public ResponseResult<CreditVo> updateCredit(@RequestBody @Validated CreditUpdateDto creditUpdateDto) {
//        return ResponseResult.success(creditService.updateCredit(creditUpdateDto));
//    }

    /**
     * 获取银行卡信息
     * @param creditGetDto 获取银行卡信息DTO
     * @return ResponseResult对象
     */
    @GetMapping
    @Operation(summary = "获取银行卡信息")
    public ResponseResult<CreditVo> getCredit(@RequestBody @Validated CreditGetDto creditGetDto) {
        return ResponseResult.success(creditService.getCredit(creditGetDto.getCardNumber()));
    }
}
