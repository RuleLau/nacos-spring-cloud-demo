package com.rule;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})
@MapperScan("com.rule.mapper")
public class ShardingJdbcMybatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingJdbcMybatisApplication.class, args);
    }

}
