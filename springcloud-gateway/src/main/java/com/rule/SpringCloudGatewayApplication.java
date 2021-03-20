package com.rule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient  // 启用服务注册和发现
public class SpringCloudGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudGatewayApplication.class, args);
    }

    /**
     * 代码方式设置路由，也可以在yml文件中设置路由信息
     * @param builder
     * @return
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // 配制一个路由,把 http://网关地址:网关端口/demo/ 下的请求路由到 demo-service 微服务中
                .route(p -> p
                        .path("/echo/**") //url匹配
                        .uri("lb://nacos-service-provider"))  // 将请求路由到指定目标, lb开头是注册中心中的服务, http/https 开头你懂的
                .build();
    }
}
