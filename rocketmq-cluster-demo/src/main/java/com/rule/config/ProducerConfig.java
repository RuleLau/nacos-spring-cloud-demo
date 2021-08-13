package com.rule.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix = "rocketmq.producer")
@Configuration
@ToString
public class ProducerConfig {
    private String namesrvAddr;

    private String groupName;
}