//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.jxau.config;

import com.jxau.interceptors.FeginInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeginSupportHeader {
    public FeginSupportHeader() {
    }

    @Bean
    public RequestInterceptor getRequestInterceptor() {
        return new FeginInterceptor();
    }
}
