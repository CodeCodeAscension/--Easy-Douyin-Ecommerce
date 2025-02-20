package com.example.common.exception;

import com.example.common.domain.ResultCode;

public class NotFoundException extends UserException {

    public NotFoundException() {
        super(ResultCode.NOT_FOUND, "请求错误");
    }

    public NotFoundException(String message) {
        super(ResultCode.NOT_FOUND, message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(ResultCode.NOT_FOUND, message, cause);
    }

    public NotFoundException(Throwable cause) {
        super(ResultCode.NOT_FOUND, cause);
    }
}
