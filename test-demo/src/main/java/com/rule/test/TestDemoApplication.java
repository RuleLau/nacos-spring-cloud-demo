package com.rule.test;

import com.custom.configuration.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestDemoApplication {

	@Autowired
	JSONUtil jsonUtil;

	public static void main(String[] args) {
		SpringApplication.run(TestDemoApplication.class, args);
	}

}
