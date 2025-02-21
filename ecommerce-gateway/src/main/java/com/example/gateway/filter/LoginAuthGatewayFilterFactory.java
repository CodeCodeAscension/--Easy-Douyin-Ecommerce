package com.example.gateway.filter;

import com.example.common.exception.SystemException;
import com.example.common.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

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
                throw new UnauthorizedException("用户未登录");
            } catch (Exception e) {
                // 出现了未知的异常，记录日志
                log.error(e.getMessage());
                throw new SystemException(e);
            }
            return chain.filter(exchange);
        };
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
