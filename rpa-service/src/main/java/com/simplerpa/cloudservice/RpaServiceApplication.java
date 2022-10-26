package com.simplerpa.cloudservice;

import com.ruoyi.common.security.annotation.EnableCustomConfig;
import com.ruoyi.common.security.annotation.EnableRyFeignClients;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Description: TODO
 * @author: ChenRui98
 * @date: 2022年04月07日 21:01
 */

@SpringBootApplication
@EnableCustomConfig
@EnableRyFeignClients
@MapperScan("com.simplerpa.cloudservice.mapper")
public class RpaServiceApplication {
    public static void main(String[] args){
        SpringApplication.run(RpaServiceApplication.class, args);
    }
}
