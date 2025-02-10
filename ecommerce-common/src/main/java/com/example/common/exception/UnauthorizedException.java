package com.example.common.exception;

import com.example.common.domain.ResultCode;

/**
 * 未授权异常
 */
public class UnauthorizedException extends UserException {

    public UnauthorizedException() {
        super(ResultCode.UNAUTHORIZED, "请先登录！");
    }

    public UnauthorizedException(String message) {
        super(ResultCode.UNAUTHORIZED, message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(ResultCode.UNAUTHORIZED, message, cause);
    }
}
