package com.example.test.controller;

import com.example.api.client.UserClient;
import com.example.common.domain.ResponseResult;
import com.example.test.domain.TestPo;
import com.example.test.mapper.TestMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "测试接口")
@Slf4j
public class TestController {

    @Autowired
    private TestMapper testMapper;

    @GetMapping("/mybatis")
    @ApiOperation("test")
    public List<TestPo> test() {
        List<TestPo> testPoList = testMapper.selectList(null);
        return testPoList;
    }

    @Autowired
    private UserClient userClient;

    @GetMapping("/users")
    public ResponseResult getUsers() {
        return userClient.getUserInfo();
    }
}
