package com.example.common.advice;

import com.example.common.domain.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
@Slf4j
public class ResultAdvice implements ResponseBodyAdvice<ResponseResult> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return ResponseResult.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public ResponseResult beforeBodyWrite(ResponseResult body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        try {
            int code = body.getCode();
            response.setStatusCode(HttpStatus.valueOf(code));
        } catch (Exception e) {
            log.error("Controller返回了未知的状态码："+body.getCode());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return body;
    }
}
