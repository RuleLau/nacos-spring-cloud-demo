package com.rule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient  // 启用服务注册和发现
public class SpringcloudAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringcloudAuthApplication.class, args);
    }

}
