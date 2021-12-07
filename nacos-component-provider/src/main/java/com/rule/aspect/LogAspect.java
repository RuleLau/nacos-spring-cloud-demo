package com.rule.aspect;

import com.rule.annotation.Log;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
public class LogAspect {

    @Pointcut("execution (* com.rule.service..*.*(..))")
    public void logPointcut() {

    }

    @Around("logPointcut()")
    public void around(JoinPoint point) {
        String methodName = point.getSignature().getName();
        Object[] args = point.getArgs();
        Class<?>[] argTypes = new Class[point.getArgs().length];
        for (int i = 0; i < args.length; i++) {
            argTypes[i] = args[i].getClass();
        }
        Method method = null;
        try {
            method = point.getTarget().getClass().getMethod(methodName, argTypes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获取方法上的注解
        Log log = method.getAnnotation(Log.class);
        if (log != null) {
            //演示方法执行前，记录一行日志
            System.out.println("before:" + methodName);
        }
        try {
            //执行方法
            ((ProceedingJoinPoint) point).proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            if (log != null) {
                //演示方法执行后，记录一行日志
                System.out.println("after:" + methodName);
            }
        }
    }
}
