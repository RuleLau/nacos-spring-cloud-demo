package com.rule.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;

@RestController
@RefreshScope
public class HystrixController {

    /**
     * 这个方法会造成服务调用超时的错误
     * 其实本身体不是错误，而是服务响应时间超过了我们要求的时间，就认为它错了
     *
     * @param id
     * @return
     */
    @HystrixCommand(fallbackMethod = "TimeOutErrorHandler")
    @GetMapping(value = "/hystrixI/{id}")
    public String timeOutError(@PathVariable Integer id) {
        try {
            //我们让这个方法休眠5秒，所以一定会发生错误，也就会调用下边的fallbakcMethod方法
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "服务正常调用" + id;
    }

    @GetMapping(value = "/test/{id}")
    public String test(@PathVariable Integer id) {
        return "服务正常调用" + id;
    }

    /**
     * 这个就是当上边方法的“兜底”方法
     */
    public String TimeOutErrorHandler(Integer id) {
        return "对不起，系统处理超时" + id;
    }


/*    @Value("${demo}")
    private String demo;

    @Value("${test-01}")
    private String test01;

    @Value("${test-02}")
    private String test02;*/

/*
    @GetMapping(value = "/demo")
    public String demo(){
        return demo;
    }

    @GetMapping(value = "/test")
    public String test(){
        return test01;
    }
    @GetMapping(value = "/test2")
    public String test2(){
        return test02;
    }
*/
    interface Subject {
        String sayHi(String msg);
        void sayHello();
    }

    static class SubjectImpl implements Subject {

        @Override
        public String sayHi(String msg) {
            System.out.println("hi");
            return msg;
        }

        @Override
        public void sayHello() {
            System.out.println("hello");
        }
    }

    static class ProxyInvocationHandler implements InvocationHandler {
        private Subject target;

        public ProxyInvocationHandler(Subject target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.print("say:");
            method.invoke(target, args);
            return "bb";
        }
    }

    public static void main(String[] args) {
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        Subject subject = new SubjectImpl();
        Subject subjectProxy = (Subject) Proxy.newProxyInstance(SubjectImpl.class.getClassLoader(),
                SubjectImpl.class.getInterfaces(), new ProxyInvocationHandler(subject));
        String s = subjectProxy.sayHi("hi");
        System.out.println(s);
        subjectProxy.sayHello();
    }
}
