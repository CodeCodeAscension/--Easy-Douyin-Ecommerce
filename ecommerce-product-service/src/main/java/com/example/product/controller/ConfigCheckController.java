package com.example.product.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RefreshScope
public class ConfigCheckController {

    @Value("${ecommerce.elasticsearch.host}")
    private String esHost;

    @Value("${ecommerce.elasticsearch.port}")
    private String esPort;

    @GetMapping("/config/es")
    public Map<String, String> checkEsConfig() {
        return Map.of(
                "host", esHost,
                "port", esPort
        );
    }
}
