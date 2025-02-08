package com.example.api.client;

import com.example.api.domain.dto.cart.AddItemDto;
import com.example.api.domain.dto.cart.EmptyCartDto;
import com.example.api.domain.dto.cart.GetCartDto;
import com.example.api.domain.vo.cart.AddItemVo;
import com.example.api.domain.vo.cart.EmptyCartVo;
import com.example.api.domain.vo.cart.GetCartVo;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("cart-service")
public interface CartClient {
    AddItemVo addItem(AddItemDto addItemDto);
    GetCartVo getCart(GetCartDto getCartDto);
    EmptyCartVo emptyCart(EmptyCartDto emptyCartDto);
}
