package com.example.demo.config;

import com.example.demo.common.interceptor.LoginCheckInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//인터셉터 작동시키기위한 스프링 등록 장부 느낌
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**") //모든 URL에 적용
                .excludePathPatterns(
                        "/", "/members/join", "/members/login",
                        "/css/**", "/js/**", "/images/**",
                        "/favicon.ico",
                        "/error", "/h2-console/**"
                );
    }
}
