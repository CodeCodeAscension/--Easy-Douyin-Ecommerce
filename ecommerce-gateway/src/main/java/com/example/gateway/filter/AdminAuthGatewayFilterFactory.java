package com.example.gateway.filter;

import com.example.auth.enums.UserPower;
import com.example.common.domain.ResultCode;
import com.example.common.exception.SystemException;
import com.example.common.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * <p>
 *     管理员权限验证过滤器，判断用户是否为管理员
 *     在LoginAuthGatewayFilterFactory后执行，直接判断X-User-Power请求头
 * </p>
 * @author vlsmb
 */
@Component
@Slf4j
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
                throw new UserException(ResultCode.FORBIDDEN, "当前接口仅允许管理员访问");
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
        return 2;
    }
}
