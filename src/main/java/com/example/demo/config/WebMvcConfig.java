package com.example.demo.config;

import com.example.demo.common.interceptor.LoginCheckInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
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

    // spring.web.resources.add-mappings=false 로 기본 정적 리소스 매핑이 꺼져있어 직접 등록
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");
    }
}
