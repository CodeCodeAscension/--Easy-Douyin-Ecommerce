package com.example.user;

import com.example.auth.domain.UserClaims;
import com.example.user.config.UserServiceConfig;
import com.example.user.domain.po.User;
import com.example.auth.enums.UserPower;
import com.example.user.enums.UserStatusEnum;
import com.example.user.mapper.UserMapper;
import com.example.auth.util.BCryptUtil;
import com.example.auth.util.JwtUtil;
import com.example.auth.util.TokenRedisUtil;
import com.example.auth.config.JwtConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class UserTest {
    @Autowired
    private BCryptUtil bCryptUtil;

    @Test
    public void testBCrypt() {
//        String hashPwd = bCryptUtil.hashPassword("123456");
        String hashPwd = "$2a$10$hphd7MMTad876ujm0HpQvuqne4VNfNhTqtMnCSryjYigHiXVwfO/C";
        System.out.println(bCryptUtil.matches("123456",hashPwd));
    }

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testMapper() {
        User user = new User();
        user.setEmail("123@qq.com");
        user.setPassword("123456");
        user.setPower(UserPower.ADMIN);
        user.setUserName("test");
        user.setPhone("123123");
        user.setCity("test");
        user.setProvince("test");
        user.setCountry("test");
        user.setZipCode("123");
        user.setUserCurrency("test");
        user.setStatus(UserStatusEnum.BANNED);
        user.setDisableReason("test");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        if(userMapper.insert(user)!=1) {
            System.out.println("插入异常");
        }
        System.out.println("UserId: "+user.getUserId());
    }

    @Autowired
    private UserServiceConfig userServiceConfig;
    @Autowired
    private JwtConfig jwtConfig;

    @Test
    public void testValue() {
        System.out.println(jwtConfig);
    }

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void testJwt() {
        UserClaims userClaims = new UserClaims();
        userClaims.setUserId(123456L);
        userClaims.setUserPower(1);
        String token = jwtUtil.generateAccessToken(userClaims);
        System.out.println(token);
    }

    @Test
    public void testJwt2() {
//        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjbGFpbXMiOnsidXNlcklkIjoxMjM0NTYsInVzZXJQb3dlciI6MX0sImV4cCI6MTczOTczMjQ5NX0.rOm0d8_2m-DOmcP52Q-fDVemNy_4pcSf0TwoeYFTUFc";
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjbGFpbXMiOnsidXNlcklkIjoxODkxMTc2NTM5MjcyNTIzNzc4LCJ1c2VyUG93ZXIiOjB9LCJleHAiOjE3Mzk3MzQ3ODl9.ouRPN4JQIE2AQ5UyXb16GxljWh7yinJK-xKAcLi_UPM";
        System.out.println(jwtUtil.verifyToken(token));
    }

    @Autowired
    private TokenRedisUtil redisUtil;

    @Test
    public void testRedis() {
//        redisUtil.addToken(123L,"vlsmb");
        System.out.println(redisUtil.getAccessToken(123L));
//        System.out.println(redisUtil.getValidMinutes(123L));
    }
}
