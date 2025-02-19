package com.example.gateway.filter;

import com.example.auth.enums.UserPower;
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
 *     管理员权限验证过滤器，判断用户是否为管理员
 *     在LoginAuthGatewayFilterFactory后执行，直接判断X-User-Power请求头
 * </p>
 * @author vlsmb
 */
@Component
public class AdminAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> implements Ordered {
    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            try {
                Integer userPower = Integer.valueOf(Objects.requireNonNull(request.getHeaders().getFirst("X-User-Power")));
                if(userPower != UserPower.ADMIN.getCode()){
                    throw new NumberFormatException("无权限");
                }
            } catch (NumberFormatException e) {
                // 不存在用户ID，说明没有登陆
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.FORBIDDEN);
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                // 自定义响应体
                String responseBody = "{\"code\":403,\"msg\":\"当前接口仅允许管理员访问\",\"data\":null}";
                DataBuffer buffer = response.bufferFactory().wrap(responseBody.getBytes(StandardCharsets.UTF_8));
                return response.writeWith(Mono.just(buffer));
            }
            return chain.filter(exchange);
        };
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
