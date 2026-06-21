package com.taskmanager.interceptor;

import com.taskmanager.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        HttpSession session = request.getSession();

        User currentUser =
                (User) session.getAttribute("user");

        // Chưa đăng nhập
        if (currentUser == null) {

            response.sendRedirect("/login");

            return false;
        }

        String uri = request.getRequestURI();

        // USER không được vào trang quản lý người dùng
        if (uri.startsWith("/users")
                && !"ADMIN".equals(currentUser.getRole())) {

            response.sendRedirect("/");

            return false;
        }

        return true;
    }
}