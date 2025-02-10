package com.example.common.advice;

import com.example.common.domain.ResponseResult;
import com.example.common.domain.ResultCode;
import com.example.common.exception.SystemException;
import com.example.common.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局拦截所有Controller发生的异常，记录日志并给前端返回RensposeResult
 */
@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(UserException.class)
    public ResponseResult<Object> userException(UserException e) {
        log.info("UserException: "+e.getMessage(), e);
        return ResponseResult.error(e.getCode(), e.getMessage());
    }

    /**
     * Controller中参数上@Valided注解验证失败引发的异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseResult<Object> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.info("UserException: 用户输入了无效的参数");
        return ResponseResult.error(ResultCode.BAD_REQUEST, "参数无效");
    }

    @ExceptionHandler(SystemException.class)
    public ResponseResult<Object> systemException(SystemException e) {
        log.error("SystemException: "+e.getMessage(), e);
        return ResponseResult.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult<Object> exception(Exception e) {
        log.error("UnknownException: "+e.getMessage(), e);
        return ResponseResult.error(ResultCode.SERVER_ERROR, "未知的异常");
    }
}
