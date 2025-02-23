package com.example.payment.service.impl;

import com.example.common.util.UserContextUtil;

import java.lang.reflect.Field;


import com.example.common.util.UserContextUtil;
import java.lang.reflect.Field;

public class UserContextTestUtils {

    /**
     * 模拟用户登录
     * @param userId 用户ID
     */
    public static void mockLogin(Long userId) {
        try {
            // 1. 获取 UserContextUtil 的私有静态字段 "threadLocal"
            Field field = UserContextUtil.class.getDeclaredField("threadLocal");
            field.setAccessible(true); // 允许访问私有字段

            // 2. 获取当前线程的 ThreadLocal 实例
            ThreadLocal<Long> threadLocal = (ThreadLocal<Long>) field.get(null);

            // 3. 设置模拟的用户ID
            threadLocal.set(userId);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("模拟用户登录失败", e);
        }
    }

    /**
     * 清理用户登录状态
     */
    public static void clear() {
        try {
            Field field = UserContextUtil.class.getDeclaredField("threadLocal");
            field.setAccessible(true);
            ThreadLocal<Long> threadLocal = (ThreadLocal<Long>) field.get(null);
            threadLocal.remove(); // 清除线程中的用户ID
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("清理用户状态失败", e);
        }
    }
}