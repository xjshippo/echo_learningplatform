package com.jxau;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class QuestionServiceMain8085 {
    public static void main(String[] args) {
        SpringApplication.run(QuestionServiceMain8085.class,args);
    }
}
