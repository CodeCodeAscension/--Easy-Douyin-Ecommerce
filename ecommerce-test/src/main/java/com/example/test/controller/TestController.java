package com.example.test.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "测试接口")
@Slf4j
public class TestController {

    @GetMapping
    @ApiOperation("test")
    public String test() {
        log.info("test");
        return "test";
    }
}
