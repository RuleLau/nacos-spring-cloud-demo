package com.rule.consumer;


import lombok.SneakyThrows;

/**
 * 多线程消费
 */
public class ConsumerDemo6 {


    public static void main(String[] args) throws Exception {
        Thread.currentThread().interrupt();
//        Thread.sleep(3000);
        System.out.println(Thread.interrupted());

        System.out.println(Thread.interrupted());
    }

   @SneakyThrows
    public static void throwInterupt() {
        if (1 + 1 == 2) {
            throw new InterruptedException();
        }
    }
}
