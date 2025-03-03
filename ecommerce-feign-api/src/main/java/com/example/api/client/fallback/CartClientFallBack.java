package com.example.api.client.fallback;

import com.example.api.client.CartClient;
import com.example.api.domain.po.CartItem;
import com.example.api.domain.vo.cart.CartInfoVo;
import com.example.common.domain.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CartClientFallBack implements FallbackFactory<CartClient> {
    @Override
    public CartClient create(Throwable cause) {
        return new CartClient() {
            @Override
            public ResponseResult<CartInfoVo> getCartInfo() {
                log.error("cart-service-exception:getCartInfo, "+cause.getMessage());
                return ResponseResult.errorFeign(cause);
            }

            @Override
            public ResponseResult<CartItem> getCartItem(Long id) {
                log.error("cart-service-exception:getCartItem, "+cause.getMessage());
                return ResponseResult.errorFeign(cause);
            }
        };
    }
}
