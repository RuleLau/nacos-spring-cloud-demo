package com.rule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;

@SpringBootApplication
// 开启单点登录
@EnableOAuth2Sso
public class OauthClient2Application {

    public static void main(String[] args) {
        SpringApplication.run(OauthClient2Application.class, args);
    }

}
