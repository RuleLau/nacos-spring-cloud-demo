package com.custom.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonConfiguration {

    @Bean
    public JSONUtil jsonUtil(){
        return new JSONUtil();
    }

}
