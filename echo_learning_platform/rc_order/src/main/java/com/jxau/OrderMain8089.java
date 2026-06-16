package com.jxau;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.Ordered;

@SpringBootApplication(exclude= DataSourceAutoConfiguration.class )
@EnableDiscoveryClient
@EnableFeignClients
public class OrderMain8089 {
    public static void main(String[] args) {
        SpringApplication.run(OrderMain8089.class,args);
    }
}
