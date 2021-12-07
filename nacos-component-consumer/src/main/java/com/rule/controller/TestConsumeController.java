package com.rule.controller;

import com.rule.LoadBalancer;
import com.rule.Test;
import com.rule.client.HystrixClient;
import com.rule.client.ProviderClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestConsumeController {

    @Autowired
    @LoadBalanced
    @Qualifier("xxxTemplate")
    private RestTemplate restTemplate;

    @Autowired
    @LoadBalancer
    private List<Test> testList = Collections.emptyList();

    @Autowired
    private HystrixClient hystrixClient;

    @Autowired
    private ProviderClient providerClient;

    @GetMapping(value = "/hystrixI/{id}")
    public String testHystrixI(@PathVariable Integer id){
        return providerClient.test( id);
    }

    @GetMapping(value = "/hystrixII/{id}")
    public String testRestTemplate(@PathVariable Integer id){
        return restTemplate.getForObject("http://nacos-component-provider/hystrixI/" + id, String.class);
    }
}
