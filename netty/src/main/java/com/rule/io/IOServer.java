package com.rule.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * IO 模型
 * 1. 每个线程维护一个连接，线程数有限，连接很多的情况，则会出现 CPU 的线程竞争问题
 * 2. 线程资源受限,同一时刻有大量的线程处于阻塞状态是非常严重的资源浪费，操作系统耗不起
 * 3. IO编程中，我们看到数据读写是以字节流为单位，效率不高。
 *
 */
public class IOServer {

    public static void main(String[] args) throws Exception{

        ServerSocket serverSocket = new ServerSocket(8000);
        // 接收新连接的线程
        new Thread(() -> {
            while (true) {
                try {
                    // 阻塞方法获取新的连接
                    System.out.println(Thread.currentThread().getName() + "开始接收新连接的线程");
                    Socket socket = serverSocket.accept();
                    // 每一个新的连接都创建一个线程，负责读取数据
                    new Thread(() -> {
                        try {
                            System.out.println(Thread.currentThread().getName() + "开始读取数据");
                            byte[] data = new byte[1024];
                            InputStream inputStream = socket.getInputStream();
                            while (true) {
                                int len;
                                // 按照字节流的方式读取数据
                                System.out.println("输出数据：");
                                while ((len = inputStream.read(data)) != -1) {
                                    System.out.println(new String(data, 0, len));
                                }
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
