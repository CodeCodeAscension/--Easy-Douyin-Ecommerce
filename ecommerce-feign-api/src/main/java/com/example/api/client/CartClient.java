package com.example.api.client;

import com.example.api.client.fallback.CartClientFallBack;
import com.example.api.domain.vo.cart.CartInfoVo;
import com.example.common.domain.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "cart-service", fallbackFactory = CartClientFallBack.class)
public interface CartClient {
    // 获得当前用户的购物车信息
    @GetMapping("/api/v1/carts")
    ResponseResult<CartInfoVo> getCartInfo();
}
