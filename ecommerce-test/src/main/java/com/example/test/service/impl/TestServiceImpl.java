package com.example.test.service.impl;

import com.example.api.client.OrderClient;
import com.example.api.domain.dto.order.PlaceOrderDto;
import com.example.common.util.UserContextUtil;
import com.example.test.domain.TestPo;
import com.example.test.mapper.TestMapper;
import com.example.test.service.TestService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestMapper testMapper;
    @Autowired
    private OrderClient orderClient;

    @Override
    @GlobalTransactional
    public void testSeata() {
        TestPo testPo = new TestPo();
        testPo.setName("testSeata");
        testPo.setPwd("123456");
        testMapper.insert(testPo);
        UserContextUtil.setUserId(123456L);
        System.out.println(orderClient.placeOrder(new PlaceOrderDto()).getMsg());
        int i=1/0;
    }
}
