package com.example.api.client;

import com.example.api.domain.dto.checkout.CheckoutDto;
import com.example.api.domain.vo.checkout.CheckoutVo;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("checkout-service")
public interface CheckoutClient {
    CheckoutVo checkout(CheckoutDto checkoutDto);
}
