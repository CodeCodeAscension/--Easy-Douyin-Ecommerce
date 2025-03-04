package com.example.api.client;

import com.example.api.client.fallback.CartClientFallBack;
import com.example.api.domain.po.CartItem;
import com.example.common.domain.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "cart-service", fallbackFactory = CartClientFallBack.class)
public interface CartClient {
    // 获得某个CartItem的信息
    @GetMapping("/api/v1/carts/items/{id}")
    ResponseResult<CartItem> getCartItem(@PathVariable Long id);
}
