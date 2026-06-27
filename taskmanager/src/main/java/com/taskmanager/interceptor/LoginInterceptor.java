package com.taskmanager.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession(false);

        // Nếu chưa đăng nhập
        if (session == null || session.getAttribute("user") == null) {

            response.sendRedirect("/login");
            return false;
        }

        // Đã đăng nhập
        return true;
    }
}