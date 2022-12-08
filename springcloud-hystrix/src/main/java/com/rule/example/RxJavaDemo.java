package com.rule.example;

import rx.Observable;
import rx.Observer;
import rx.functions.Action0;
import rx.functions.Func0;

import java.util.concurrent.ExecutionException;

public class RxJavaDemo {

    // ReactiveX Java  响应式编程框架(android）
    // Java stream() java8
    //观察者模式
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final String[] datas = new String[]{"登录"};

        final Action0 onComplated = () -> System.out.println("on Complated: observable");
        //老师（被观察者）
        Observable<String> observable = Observable.defer((Func0<Observable<String>>) () -> {
            Observable observable1 = Observable.from(datas);
//            int a = 1 / 0;
            return observable1.doOnCompleted(onComplated);
        });
        //学生(观察者)
        Observer observer = new Observer() {
            @Override
            public void onCompleted() {
                System.out.println("Observer: onCompleted");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Observer: onError");
            }

            @Override
            public void onNext(Object o) {
                System.out.println("on Next:" + o);
            }
        };
        observable.subscribe(observer); //建立订阅关系

        String s = observable.toBlocking().toFuture().get();//建立订阅关系
        observer.onCompleted();
        System.out.println(s);
    }
}