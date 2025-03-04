package com.example.api.client.fallback;

import com.example.api.client.CartClient;
import com.example.common.domain.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CartClientFallBack implements FallbackFactory<CartClient> {
    @Override
    public CartClient create(Throwable cause) {
        return id -> {
            log.error("cart-service-exception:getCartItem, {}", cause.getMessage());
            return ResponseResult.errorFeign(cause);
        };
    }
}
