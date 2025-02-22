package com.example.payment.service.impl;

import com.example.common.domain.ResponseResult;
import com.example.common.util.UserContextUtil;
import com.example.payment.domain.po.Credit;
import com.example.payment.domain.po.Transaction;
import com.example.payment.enums.PaymentStatusEnum;
import com.example.payment.mapper.CreditMapper;
import com.example.payment.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ConcurrentModificationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CreditServiceImplCancelTest {

    @Mock
    private CreditMapper creditMapper;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private CreditServiceImpl creditService;

    private final String validTransactionId = "TX_123";
    private final String validCreditId = "CREDIT_456";
    private final Long validUserId = 1L;
    private final Float transactionAmount = 100.0f;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        UserContextUtil.setUserId(validUserId);
    }

    @Test
    void cancelCharge_UserNotLoggedIn_ShouldFail() {
        UserContextUtil.setUserId(null);

        ResponseResult<?> result = creditService.cancelCharge(validTransactionId);

        assertEquals(PaymentStatusEnum.USER_NOT_LOGIN.getErrorCode(), result.getCode());
    }

    @Test
    void cancelCharge_TransactionNotFound_ShouldFail() {
        when(transactionService.getById(validTransactionId)).thenReturn(null);
        ResponseResult<?> result = creditService.cancelCharge(validTransactionId);

        assertEquals(PaymentStatusEnum.PAYMENT_NOT_FOUND.getErrorCode(), result.getCode());
    }

    @Test
    void cancelCharge_UnauthorizedUser_ShouldFail() {
        Transaction tx = buildTransaction(validUserId + 1); // 其他用户的交易

        when(transactionService.getById(validTransactionId)).thenReturn(tx);

        ResponseResult<?> result = creditService.cancelCharge(validTransactionId);

        assertEquals(PaymentStatusEnum.PAYMENT_ACCESS_DENIED.getErrorCode(), result.getCode());
    }

    @Test
    void cancelCharge_InvalidTransactionStatus_ShouldFail() {
        Transaction tx = buildTransaction(validUserId);
        tx.setStatus(1);

        when(transactionService.getById(validTransactionId)).thenReturn(tx);

        ResponseResult<?> result = creditService.cancelCharge(validTransactionId);

        assertEquals(PaymentStatusEnum.PAYMENT_CANNOT_CANCEL.getErrorCode(), result.getCode());
    }

    @Test
    void cancelCharge_CreditNotAvailable_ShouldFail() {
        Transaction tx = buildTransaction(validUserId);
        Credit invalidCredit = new Credit(validCreditId, validUserId, 200f, 1); // 状态异常

        when(transactionService.getById(validTransactionId)).thenReturn(tx);
        when(creditMapper.selectById(validCreditId)).thenReturn(invalidCredit);

        ResponseResult<?> result = creditService.cancelCharge(validTransactionId);

        assertEquals(PaymentStatusEnum.CREDIT_STATUS_ERROR.getErrorCode(), result.getCode());
    }

    @Test
    void cancelCharge_ConcurrentUpdateConflict_ShouldRollback() {
        Transaction tx = buildTransaction(validUserId);
        Credit credit = new Credit(validCreditId, validUserId, 200f, 0);

        when(transactionService.getById(validTransactionId)).thenReturn(tx);
        when(creditMapper.selectById(validCreditId)).thenReturn(credit);
        when(creditMapper.updateById(credit)).thenReturn(0);

        assertThrows(ConcurrentModificationException.class,
                () -> creditService.cancelCharge(validTransactionId));

        verify(transactionService, never()).updateById(any());
    }

    @Test
    void cancelCharge_SuccessfulCancellation_ShouldUpdateAllStatus() {
        Transaction tx = buildTransaction(validUserId);
        Credit credit = new Credit(validCreditId, validUserId, 200f, 0);

        when(transactionService.getById(validTransactionId)).thenReturn(tx);
        when(creditMapper.selectById(validCreditId)).thenReturn(credit);
        when(creditMapper.updateById(credit)).thenReturn(1);
        when(tx.getAmount()).thenReturn(100f);

        ResponseResult<?> result = creditService.cancelCharge(validTransactionId);

        assertTrue(result.getCode() == 200);
        verify(transactionService).updateById(argThat(t ->
                t.getStatus() == 1 && "用户取消支付".equals(t.getReason())
        ));
        verify(creditMapper).updateById(argThat(c -> c.getBalance() == 300f));
    }

    private Transaction buildTransaction(Long userId) {
        return Transaction.builder()
                .transId(validTransactionId)
                .userId(userId)
                .creditId(validCreditId)
                .amount(transactionAmount)
                .status(1)
                .build();
    }
}