package com.rule.controller;

import com.rule.client.ProviderClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class TestConsumeController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ProviderClient providerClient;

    @GetMapping(value = "/echo/{str}")
    public String echo(@PathVariable String str) {
        return restTemplate.getForObject("http://nacos-service-provider/echo/" + str, String.class);
    }

    @GetMapping(value = "/echoII/{str}")
    public String echoII(@PathVariable String str) {
        return providerClient.echo(str);
    }
}
