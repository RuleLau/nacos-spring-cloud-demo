package com.rule.bio;

import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class TcpClient {

    public static void main(String[] args) throws Exception {
        // 1.创建客户端Scoket对象，输入服务器的IP和端口号，请求连接服务器
        Socket client = new Socket();
        InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 8080);
        System.out.println("开始连接 socket 服务器");
        client.connect(socketAddress);
        // 2.通过Socket管道得到一个字节输出流
        OutputStream outputStream = client.getOutputStream();
        // 3.将字节输出流包装成高级的字节打印流
        PrintStream ps = new PrintStream(outputStream);
        // 4.通过字节打印流将数据发送到服务器端
        while (true) {
            System.out.println("send data：");
            Scanner sc = new Scanner(System.in);
            ps.println(sc.nextLine());
            ps.flush();
        }
    }
}
