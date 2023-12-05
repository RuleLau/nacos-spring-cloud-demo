package com.rule.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@RestController
@Slf4j
public class ConsumeController {

    @GetMapping(value = "/echo")
    public String echo(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            log.info("headerName: {}, value: {}.", headerName, request.getHeader(headerName));
        }
        return "say hi";
    }

}
