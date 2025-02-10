package com.example.common.exception;

import com.example.common.domain.ResultCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 由于用户的操作而产生的异常（如数据不存在）
 */
@Getter
@Slf4j
public class UserException extends RuntimeException {

    private int code;

    public UserException(int code, String message) {
        super(message);
        log.info("UserException: {}", message);
        this.code = code;
    }

    public UserException(int code, String message, Throwable cause) {
        super(message, cause);
        log.info("UserException: {}, cause: {}", message, cause);
        this.code = code;
    }

    public UserException(int code, Throwable cause) {
        super(cause);
        log.info("UserException, cause: {}", cause);
        this.code = code;
    }
}
