package com.example.payment.enums;

import com.example.common.domain.ResponseResult;
import com.example.common.domain.ResultCode;
import com.example.payment.exception.BaseExceptionInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatusEnum implements BaseExceptionInterface {
    // =================================== 通用异常码 ===================================
    SYSTEM_ERROR(ResultCode.SERVER_ERROR, "出错啦，后台小哥正在努力修复中..."),

    //=================================== 业务异常状态码 ===================================
    ORDER_ID_IS_NULL(ResultCode.NOT_FOUND, "订单id为空"),
    USER_NOT_LOGIN(ResultCode.UNAUTHORIZED, "用户未登录"),
    PAYMENT_FAILED(ResultCode.BAD_REQUEST, "支付失败"),
    ORDER_NOT_EXIST(ResultCode.NOT_FOUND, "订单不存在或查询失败"),
    ORDER_STATUS_ERROR(ResultCode.BAD_REQUEST, "订单状态异常"),
    CREDIT_NOT_EXISTED(ResultCode.NOT_FOUND, "该银行卡不存在"),
    CREDIT_STATUS_ERROR(ResultCode.FORBIDDEN, "该银行卡被禁用或已过期"),
    PAYMENT_CONFLICT(ResultCode.CONFLICT, "并发冲突导致支付失败"),
    PAYMENT_NOT_FOUND(ResultCode.NOT_FOUND, "交易记录不存在"),
    PAYMENT_ACCESS_DENIED(ResultCode.UNAUTHORIZED, "无权访问该交易记录"),
    PAYMENT_CANNOT_CANCEL(ResultCode.FORBIDDEN, "该交易记录无法取消"),
    ;


    // 异常码
    private Integer errorCode;

    //错误信息
    private String errorMessage;
}
