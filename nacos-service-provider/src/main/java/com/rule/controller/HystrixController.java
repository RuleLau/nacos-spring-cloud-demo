package com.rule.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class HystrixController {

     /**
     * 这个方法会造成服务调用超时的错误
     * 其实本身体不是错误，而是服务响应时间超过了我们要求的时间，就认为它错了
     * @param id
     * @return
     */
//    @HystrixCommand(fallbackMethod = "TimeOutErrorHandler")
    @GetMapping(value = "/hystrixI/{id}")
    public String timeOutError(@PathVariable Integer id){
        try {
            //我们让这个方法休眠5秒，所以一定会发生错误，也就会调用下边的fallbakcMethod方法
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "服务正常调用"+id;
    }

    /**
     *	这个就是当上边方法的“兜底”方法
     */
    public String TimeOutErrorHandler(Integer id) {
        return "对不起，系统处理超时"+id;
    }
}
