package com.rule.demo;

import com.rule.client.ConsumerClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@RestController
@Slf4j
public class EchoController {

    @Autowired
    private ConsumerClient consumerClient;

    @GetMapping(value = "/echo")
    public String echo() {
        return "Hello Nacos Discovery ";
    }

    @GetMapping(value = "/call/feign")
    public String callFeign(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            log.info("headerName: {}, value: {}.", headerName, request.getHeader(headerName));
        }
        return consumerClient.echo();
    }
}
