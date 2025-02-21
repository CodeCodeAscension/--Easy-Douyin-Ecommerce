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
    ;

    // 异常码
    private Integer errorCode;

    //错误信息
    private String errorMessage;
}
