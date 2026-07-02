package com.taskmanager.interceptor;

import com.taskmanager.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        User user = (User) request.getSession().getAttribute("user");

        // Chưa đăng nhập
        if (user == null) {
            response.sendRedirect("/login");
            return false;
        }

        String uri = request.getRequestURI();

        // Chỉ ADMIN mới được truy cập /users
        if (uri.startsWith("/users")
                && !"ADMIN".equalsIgnoreCase(user.getRole())) {

            response.sendRedirect("/");
            return false;
        }

        return true;
    }
}