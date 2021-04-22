package com.rule.controller;

import com.rule.client.HystrixClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/test")
public class TestConsumeController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HystrixClient hystrixClient;

    @GetMapping(value = "/hystrixI/{id}")
    public String testHystrixI(@PathVariable Integer id){
        return restTemplate.getForObject("http://nacos-component-provider/hystrixI/" + id, String.class);
//        return hystrixClient.hystrix(id);
    }
}
