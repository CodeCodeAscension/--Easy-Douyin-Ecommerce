package com.example.api.client;

import com.example.api.domain.dto.user.DeliverTokenDto;
import com.example.api.domain.dto.user.VerifyTokenDto;
import com.example.api.domain.vo.user.DeliverTokenVo;
import com.example.api.domain.vo.user.VerifyTokenVo;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "user-service", contextId = "auth")
public interface AuthClient {
    DeliverTokenVo deliverToken(DeliverTokenDto deliverTokenDto);
    VerifyTokenVo verifyToken(VerifyTokenDto verifyTokenDto);
}
