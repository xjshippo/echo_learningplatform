package com.jxau;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author chenzouquan
 */
@MapperScan("com.jxau.mapper")
@SpringBootApplication
@EnableTransactionManagement
@EnableFeignClients
public class IndexStudyApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(IndexStudyApplication.class,args);
    }
}
