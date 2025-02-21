package com.example.gateway.filter;

import com.example.auth.domain.UserClaims;
import com.example.auth.util.JwtUtil;
import com.example.auth.util.TokenRedisUtil;
import com.example.common.exception.SystemException;
import com.example.common.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * <p>
 *     全局过滤器，如果有Token则设置对应的X-User-Id和X-User-Power请求头
 * </p>
 * @author vlsmb
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TokenAnalysisGlobalFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;
    private final TokenRedisUtil tokenRedisUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 清除请求头中X-User-Id和X-User-Power字段，防止伪造请求头
        ServerHttpRequest request = exchange.getRequest().mutate()
                .header("X-User-Id", "")
                .header("X-User-Power","")
                .build();
        // 从请求头中获取Token
        String token = request.getHeaders().getFirst("Authorization");
        if(token == null) {
            // 没有获得Token，放行到下面的权限过滤器
            return chain.filter(exchange.mutate().request(request).build());
        }

        // 解析Token并设置请求头
        try {
            UserClaims userClaims = jwtUtil.verifyToken(token);
            // 检查当前Redis里的token与现在传递进来的是否一致
            if(!token.equals(tokenRedisUtil.getAccessToken(userClaims.getUserId()))) {
                throw new UnauthorizedException("Token已失效");
            }
            // 不是RefreshToken
            if(userClaims.getIsRefreshToken()) {
                throw new UnauthorizedException("请使用AccessToken访问本页面");
            }

            // 设置请求头
            ServerHttpRequest mutateRequest = request.mutate()
                    .header("X-User-Id", userClaims.getUserId().toString())
                    .header("X-User-Power", userClaims.getUserPower().toString())
                    .build();
            return chain.filter(exchange.mutate().request(mutateRequest).build());
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException(e.getMessage());
        } catch (Exception e) {
            // 出现了未知的异常，记录日志
            log.error(e.getMessage());
            throw new SystemException(e);
        }
    }

    @Override
    public int getOrder() {
        return 0;   // 最先执行的过滤器
    }
}
