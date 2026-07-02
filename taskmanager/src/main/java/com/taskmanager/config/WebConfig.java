package com.taskmanager.config;

import com.taskmanager.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/login",
                        "/logout",

                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/avatars/**",

                        "/favicon.ico",
                        "/error",
                        "/webjars/**"
                );
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String uploadPath = System.getProperty("user.dir")
                + "/uploads/avatars/";

        registry.addResourceHandler("/avatars/**")
                .addResourceLocations("file:" + uploadPath);
    }
}