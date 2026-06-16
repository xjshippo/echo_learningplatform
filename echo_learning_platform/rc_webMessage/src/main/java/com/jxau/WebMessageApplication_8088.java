package com.jxau;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author chenzouquan
 */
@MapperScan("com.jxau.mapper")
@SpringBootApplication
@EnableTransactionManagement
public class WebMessageApplication_8088 {
    public static void main(String[] args) {
        SpringApplication.run(WebMessageApplication_8088.class,args);
    }
}
