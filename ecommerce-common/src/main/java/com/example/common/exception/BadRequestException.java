package com.example.common.exception;

import com.example.common.domain.ResultCode;

public class BadRequestException extends UserException {

    public BadRequestException() {
        super(ResultCode.BAD_REQUEST, "请求错误");
    }

    public BadRequestException(String message) {
        super(ResultCode.BAD_REQUEST, message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(ResultCode.BAD_REQUEST, message, cause);
    }

    public BadRequestException(Throwable cause) {
        super(ResultCode.BAD_REQUEST, cause);
    }
}
