package com.example.gateway.filter;

import com.example.auth.domain.UserClaims;
import com.example.auth.util.JwtUtil;
import com.example.auth.util.TokenRedisUtil;
import com.example.common.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;

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
            if(!token.equals(tokenRedisUtil.getToken(userClaims.getUserId()))) {
                throw new UnauthorizedException("Token在Redis中失效");
            }

            // 设置请求头
            ServerHttpRequest mutateRequest = request.mutate()
                    .header("X-User-Id", userClaims.getUserId().toString())
                    .header("X-User-Power", userClaims.getUserPower().toString())
                    .build();
            return chain.filter(exchange.mutate().request(mutateRequest).build());
        } catch (UnauthorizedException e) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            // 自定义响应体
            String responseBody = "{\"code\":401,\"msg\":\"用户的Token已失效\",\"data\":null}";
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
    }

    @Override
    public int getOrder() {
        return 0;   // 最先执行的过滤器
    }
}
