package com.rule;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.rule.mapper")
public class SpringcloudAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringcloudAuthApplication.class, args);
    }

}
