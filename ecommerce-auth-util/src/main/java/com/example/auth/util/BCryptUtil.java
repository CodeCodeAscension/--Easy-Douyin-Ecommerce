package com.example.auth.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * <p>
 *     生成BCrpyt以及检验密码的工具
 * </p>
 * @author vlsmb
 */
@Component
public class BCryptUtil {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 对密码进行BCrypt加密
     * @param rawPassword 原始密码
     * @return 加密后的密码
     */
    public String hashPassword(String rawPassword) {
        return bCryptPasswordEncoder.encode(rawPassword);
    }

    /**
     * 检验密码是否配对
     * @param rawPassword 待检验的密码
     * @param encodedPassword 原始密码
     * @return 检验结果
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }
}
