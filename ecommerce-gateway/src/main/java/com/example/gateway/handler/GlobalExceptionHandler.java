package com.example.gateway.handler;

import com.example.common.domain.ResponseResult;
import com.example.common.exception.UserException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * <p>
 *     网关的全局异常拦截处理
 * </p>
 * @author vlsmb
 */
@Component
@Order(-1)
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        if(response.isCommitted()) {
            return Mono.error(ex);
        }
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        ResponseResult<Object> result = new ResponseResult<>();
        result.setData(null);

        // 根据异常种类设置相应体的内容
        try {
            if(ex instanceof ResponseStatusException) {
                response.setStatusCode(((ResponseStatusException) ex).getStatusCode());
                result.setCode(((ResponseStatusException) ex).getStatusCode().value());
            } else if(ex instanceof UserException) {
                response.setStatusCode(HttpStatusCode.valueOf(((UserException) ex).getCode()));
                result.setCode(((UserException) ex).getCode());
            } else {
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                result.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setMsg("服务器异常");
        }

        // 写入响应体
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            try {
                return bufferFactory.wrap(objectMapper.writeValueAsBytes(result));
            }
            catch (Exception e) {
                log.error("Error writing response", ex);
                return bufferFactory.wrap(new byte[0]);
            }
        }));
    }
}
