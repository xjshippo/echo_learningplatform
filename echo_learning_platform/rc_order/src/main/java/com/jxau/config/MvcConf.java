//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.jxau.config;

import com.jxau.interceptors.AuthorizationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class MvcConf extends WebMvcConfigurationSupport {
    @Autowired
    private AuthorizationInterceptor authorizationInterceptor;

    public MvcConf() {
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.authorizationInterceptor).addPathPatterns(new String[]{"/**"}).excludePathPatterns(new String[]{"/**/*swagger*/**"}).excludePathPatterns(new String[]{"/train/*swagger*/**"}).excludePathPatterns(new String[]{"/swagger.html"}).excludePathPatterns(new String[]{"/v2/api-docs"}).excludePathPatterns(new String[]{"/oss/encryptParam"}).excludePathPatterns(new String[]{"/wechat/login"}).excludePathPatterns(new String[]{"/wechat/user/login"});
    }
}
