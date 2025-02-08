package com.example.test;

import com.example.api.client.UserClient;
import com.example.api.domain.dto.user.LoginDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestRun {
    @Autowired
    private UserClient userClient;

    @Test
    public void testUserClient() {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@gmail.com");
        loginDto.setPassword("123456");
        System.out.println(userClient.login(loginDto));
    }
}
