package com.example.common.interceptor;

import com.example.common.util.UserContextUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取由网关记录的userId的值
        String userId = request.getHeader("X-User-Id");
        try {
            Long id = Long.parseLong(userId);
            UserContextUtil.setUserId(id);
        } catch (Exception e) {

        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContextUtil.clear();
    }
}
