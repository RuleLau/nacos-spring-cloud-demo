package com.rule.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 修改用户名接口
 */
@FeignClient("nacos-service-consumer")
public interface UserClient {

    @PostMapping("/user")
    void updateUserInfo(@RequestParam("id") String id,
                        @RequestParam("username") String username);

}
