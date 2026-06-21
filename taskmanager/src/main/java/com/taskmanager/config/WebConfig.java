package com.taskmanager.config;

import com.taskmanager.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/", "/tasks/**", "/users/**")
                .excludePathPatterns(
                        "/login",
                        "/logout",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/avatars/**"
                );
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String uploadPath =
                System.getProperty("user.dir")
                        + "/uploads/avatars/";

        System.out.println("UPLOAD PATH = " + uploadPath);

        registry.addResourceHandler("/avatars/**")
                .addResourceLocations("file:///" + uploadPath.replace("\\", "/"));
    }
}