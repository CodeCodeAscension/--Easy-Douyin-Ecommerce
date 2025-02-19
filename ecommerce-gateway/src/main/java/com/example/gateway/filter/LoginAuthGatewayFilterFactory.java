package com.example.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * <p>
 *     用户登陆验证过滤器，判断用户是否登陆
 *     在TokenAnalysisGlobalFilter后执行，直接判断X-User-Id请求头
 * </p>
 * @author vlsmb
 */
@Component
@Slf4j
public class LoginAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> implements Ordered {
    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            try {
                Long userId = Long.parseLong(Objects.requireNonNull(request.getHeaders().getFirst("X-User-Id")));
            } catch (NumberFormatException e) {
                // 不存在用户ID，说明没有登陆
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                // 自定义响应体
                String responseBody = "{\"code\":401,\"msg\":\"用户未登录\",\"data\":null}";
                DataBuffer buffer = response.bufferFactory().wrap(responseBody.getBytes(StandardCharsets.UTF_8));
                return response.writeWith(Mono.just(buffer));
            } catch (Exception e) {
                // 出现了未知的异常，记录日志
                log.error(e.getMessage());
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                String responseBody = "{\"code\":500,\"msg\":\""+e.getMessage()+"\",\"data\":null}";
                DataBuffer buffer = response.bufferFactory().wrap(responseBody.getBytes(StandardCharsets.UTF_8));
                return response.writeWith(Mono.just(buffer));
            }
            return chain.filter(exchange);
        };
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
