package com.rule.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, String> rt(RedisConnectionFactory factory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);// 配置连接工厂
        RedisSerializer<String> stringSerializer = new StringRedisSerializer(); //RedisTemplate有自己的默认序列化的方式，不过使用默认方式，会在redis客户端查看的时候出现乱码，不便与使用
        template.setDefaultSerializer(stringSerializer);
        return template;
    }
}
