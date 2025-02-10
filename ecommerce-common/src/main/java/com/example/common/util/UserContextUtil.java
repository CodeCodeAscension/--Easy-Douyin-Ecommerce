package com.example.common.util;

public class UserContextUtil {
    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 拦截器保存UserId
     * @param userId 用户ID
     */
    public static void setUserId(Long userId) {
        threadLocal.set(userId);
    }

    /**
     * 获取当前用户UserId
     * @return 用户ID
     */
    public static Long getUserId() {
        return threadLocal.get();
    }

    /**
     * 移除保存的用户ID
     */
    public static void clear() {
        threadLocal.remove();
    }
}
