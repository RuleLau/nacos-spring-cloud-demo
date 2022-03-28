package com.rule.config;


import com.rule.pojo.MinioProperties;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Autowired
    private MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient(){
        return MinioClient.builder().endpoint(minioProperties.getUrl())
                .credentials(minioProperties.getUsername(), minioProperties.getPassword())
                .build();
    }


}
