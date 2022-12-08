package com.rule.bio;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * bio 阻塞io
 */
public class TcpServer {

    public static void main(String[] args) throws Exception {
        // new server socket
        ServerSocket serverSocket = new ServerSocket(8080);
        while (true) {
            System.out.println("开始接收 socket 请求");
            Socket accept = serverSocket.accept();
            new AcceptThread(accept).start();
        }
    }

    public static class AcceptThread extends Thread {
        private final Socket socket;

        public AcceptThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // 3.通过Socket管道得到一个字节输入流
                InputStream is = socket.getInputStream();
                // 4.将字节输入流包装成一个高级的缓冲字符输入流
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                // 5.通过缓冲字符输入流接收客户端的数据
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(socket.getRemoteSocketAddress() + ": accept data：" + line);
                }
            } catch (Exception e) {
                System.out.println(socket.getRemoteSocketAddress() + "已经断开连接");
            }
        }
    }
}
