package com.jxau;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class LogServiceMain8086 {
    public static void main(String[] args) {
        SpringApplication.run(LogServiceMain8086.class,args);
    }
}
