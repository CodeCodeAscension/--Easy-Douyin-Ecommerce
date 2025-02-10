package com.example.common.exception;

import com.example.common.domain.ResultCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 项目在所有非预期异常处（如框架异常）应该抛出本异常
 */
@Getter
@Slf4j
public class SystemException extends RuntimeException {

    int code;

    public SystemException(String message) {
        super(message);
        log.error("SystemException: {}", message);
        this.code = ResultCode.SERVER_ERROR;
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
        log.error("SystemException: {}, cause: {}", message, cause);
        this.code = ResultCode.SERVER_ERROR;
    }

    public SystemException(Throwable cause) {
        super(cause);
        log.error("SystemException, cause: {}", cause);
        this.code = ResultCode.SERVER_ERROR;
    }
}
