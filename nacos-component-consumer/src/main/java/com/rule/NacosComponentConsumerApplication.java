package com.rule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableHystrix
public class NacosComponentConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NacosComponentConsumerApplication.class, args);
	}

	@LoadBalanced
	@Bean("xxxTemplate")
	public RestTemplate restTemplate1() {
		return new RestTemplate();
	}
//	@LoadBalanced
	@Bean("yyyTemplate")
	public RestTemplate restTemplate2() {
		return new RestTemplate();
	}

	@LoadBalancer
	@Bean
	public Test test1() {
		return new Test();
	}

//	@LoadBalancer
	@Bean
	public Test test2() {
		return new Test();
	}
}
