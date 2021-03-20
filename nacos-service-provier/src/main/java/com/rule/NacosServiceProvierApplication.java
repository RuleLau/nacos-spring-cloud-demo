package com.rule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class NacosServiceProvierApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosServiceProvierApplication.class, args);
    }

}
