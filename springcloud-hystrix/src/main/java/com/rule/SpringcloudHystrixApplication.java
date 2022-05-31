package com.rule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@SpringBootApplication
@EnableHystrix
public class SpringcloudHystrixApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringcloudHystrixApplication.class, args);
	}

}
