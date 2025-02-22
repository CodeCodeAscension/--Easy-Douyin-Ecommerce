package com.example.payment.service.impl;

import com.example.api.client.OrderClient;
import com.example.api.domain.vo.order.OrderInfoVo;
import com.example.common.domain.ResponseResult;
import com.example.common.domain.ResultCode;
import com.example.common.util.UserContextUtil;
import com.example.payment.domain.dto.ChargeDto;
import com.example.payment.domain.po.Credit;
import com.example.payment.domain.vo.ChargeVo;
import com.example.payment.enums.PaymentStatusEnum;
import com.example.payment.mapper.CreditMapper;
import com.example.payment.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CreditServiceImplTest {

    @Mock
    private CreditMapper creditMapper;

    @Mock
    private TransactionService transactionService;

    @Mock
    private OrderClient orderClient;

    @InjectMocks
    private CreditServiceImpl creditService;

    private final String orderId = "ORDER_123";
    private final String creditId = "CREDIT_456";
    private final Float amount = 100.0f;
    private final Long userId = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        UserContextUtil.setUserId(userId); // 默认设置用户已登录
    }

    @Test
    void charge_UserNotLogin_ReturnError() {
        UserContextUtil.setUserId(null); // 模拟用户未登录

        ChargeDto chargeDto = new ChargeDto(orderId, creditId, amount);
        ResponseResult<ChargeVo> result = creditService.charge(chargeDto);

        assertEquals(PaymentStatusEnum.USER_NOT_LOGIN.getErrorCode(), result.getCode());
        assertFalse(result.getCode() == 200);
    }

    @Test
    void charge_OrderNotExist_ReturnError() {
        // 模拟订单查询失败
        when(orderClient.getOrderById(orderId))
                .thenReturn(ResponseResult.error(ResultCode.NOT_FOUND,"Order not found"));

        ChargeDto chargeDto = new ChargeDto(orderId, creditId, amount);
        ResponseResult<ChargeVo> result = creditService.charge(chargeDto);

        assertEquals(PaymentStatusEnum.ORDER_NOT_EXIST.getErrorCode(), result.getCode());
        verify(transactionService, never()).save(any());
    }

    @Test
    void charge_CreditNotExist_ReturnError() {
        // 模拟订单存在
        when(orderClient.getOrderById(orderId))
                .thenReturn(ResponseResult.success(new OrderInfoVo()));
        // 模拟银行卡不存在
        when(creditMapper.selectById(creditId)).thenReturn(null);

        ChargeDto chargeDto = new ChargeDto(orderId, creditId, amount);
        ResponseResult<ChargeVo> result = creditService.charge(chargeDto);

        assertEquals(PaymentStatusEnum.CREDIT_NOT_EXISTED.getErrorCode(), result.getCode());
//        verify(transactionService).save(argThat(t -> t.getStatus() == 1)); // 验证失败交易记录
    }

    @Test
    void charge_CreditStatusInvalid_ReturnError() {
        Credit credit = new Credit(creditId, userId, 200.0f, 1); // status=1（异常）
        setupCommonMocks(credit);

        ChargeDto chargeDto = new ChargeDto(orderId, creditId, amount);
        ResponseResult<ChargeVo> result = creditService.charge(chargeDto);

        assertEquals(PaymentStatusEnum.CREDIT_STATUS_ERROR.getErrorCode(), result.getCode());
        verify(transactionService).save(argThat(t -> "银行卡状态异常".equals(t.getReason())));
    }

    @Test
    void charge_InsufficientBalance_ReturnError() {
        Credit credit = new Credit(creditId, userId, 50.0f, 0); // balance=50 < 100
        setupCommonMocks(credit);

        ChargeDto chargeDto = new ChargeDto(orderId, creditId, amount);
        ResponseResult<ChargeVo> result = creditService.charge(chargeDto);

        assertEquals(PaymentStatusEnum.PAYMENT_FAILED.getErrorCode(), result.getCode());
        verify(transactionService).save(argThat(t -> "银行卡余额不足".equals(t.getReason())));
    }

    @Test
    void charge_ConcurrentUpdateConflict_ReturnError() {
        Credit credit = new Credit(creditId, userId, 200.0f, 0);
        setupCommonMocks(credit);
        when(creditMapper.updateById(any())).thenReturn(0); // 模拟更新失败

        ChargeDto chargeDto = new ChargeDto(orderId, creditId, amount);
        ResponseResult<ChargeVo> result = creditService.charge(chargeDto);

        assertEquals(PaymentStatusEnum.PAYMENT_CONFLICT.getErrorCode(), result.getCode());
        verify(transactionService).save(argThat(t -> "并发冲突导致支付失败".equals(t.getReason())));
    }

    @Test
    void charge_Success_ReturnTransactionId() {
        Credit credit = new Credit(creditId, userId, 200.0f, 0);
        setupCommonMocks(credit);
        when(creditMapper.updateById(any())).thenReturn(1); // 模拟更新成功
        when(orderClient.markOrderPaid(any())).thenReturn(ResponseResult.success());

        ChargeDto chargeDto = new ChargeDto(orderId, creditId, amount);
        ResponseResult<ChargeVo> result = creditService.charge(chargeDto);

        assertTrue(result.getCode() == 200);
        assertNotNull(result.getData().getTransactionId());
        verify(transactionService).save(argThat(t -> t.getStatus() == 0)); // 验证成功交易记录
        verify(orderClient).markOrderPaid(argThat(dto -> orderId.equals(dto.getOrderId())));
    }

    private void setupCommonMocks(Credit credit) {
        // 模拟订单存在
        when(orderClient.getOrderById(orderId))
                .thenReturn(ResponseResult.success(new OrderInfoVo()));
        // 模拟银行卡查询
        when(creditMapper.selectById(creditId)).thenReturn(credit);
    }
}