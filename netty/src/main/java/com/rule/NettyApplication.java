package com.rule;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class NettyApplication {

    public static void main(String[] args) {
//		SpringApplication.run(NettyApplication.class, args);
        //

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, 2, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

        threadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println(" this is task;");
            }
        });


    }

}
