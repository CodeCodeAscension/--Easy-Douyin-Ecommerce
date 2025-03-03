package com.example.checkout.controller.v1;


import com.example.checkout.domain.dto.CheckoutDto;
import com.example.checkout.domain.vo.CheckoutVo;
import com.example.checkout.service.CheckoutService;
import com.example.common.domain.ResponseResult;
import com.example.common.util.UserContextUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/checkout")
@Tag(name = "结算服务", description = "结算服务(自动免密支付)")
public class CheckoutController {

    @Resource
    private CheckoutService checkoutService;

    @PostMapping
    @Operation(summary = "订单结算")
    public ResponseResult<CheckoutVo> checkout(@RequestBody @Validated CheckoutDto checkoutDto) {
        Long userId = UserContextUtil.getUserId();
        return ResponseResult.success(checkoutService.checkout(userId, checkoutDto));
    }
}
