package com.jxau.config;

import com.jxau.interceptors.AuthorizationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class MvcConf extends WebMvcConfigurationSupport {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthorizationInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/**/*swagger*/**")
                .excludePathPatterns("/train/*swagger*/**")
                .excludePathPatterns("/swagger.html")
                .excludePathPatterns("/v2/api-docs")
                .excludePathPatterns("/oss/encryptParam")
                .excludePathPatterns("/wechat/login")
        .excludePathPatterns("/wechat/user/login");

    }

}

