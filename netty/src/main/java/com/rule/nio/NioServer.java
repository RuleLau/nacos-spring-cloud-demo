package com.rule.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;

public class NioServer {

    public static void main(String[] args) throws Exception {
        // 创建 server socket channel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 绑定端口
        serverSocketChannel.bind(new InetSocketAddress(8080));
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        // 创建事件查询器
        Selector selector = SelectorProvider.provider().openSelector();
        // 将 serverSocketChannel 注册到事件查询器上，并且只关注 accept 事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            // 阻塞方法，当系统有IO事件发生时
            int eventNum = selector.select();
            System.out.println("当系统有IO事件发生时, 数量：" + eventNum);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey next = iterator.next();
                iterator.remove();
                // 连接事件
                if (next.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) next.channel();
                    // 接受客户端的连接，一个 SocketChannel 代表一个 tcp 连接
                    SocketChannel accept = channel.accept();
                    // 设置 SocketChannel 为非阻塞模式
                    accept.configureBlocking(false);
                    System.out.println("服务器接收一个新的连接：" + accept.getRemoteAddress());
                    // 将 SocketChannel 注册到事件查询器上，并且只关注 READ 事件
                    accept.register(selector, SelectionKey.OP_READ);
                }
                // read 事件
                if (next.isReadable()) {
                    SocketChannel channel = (SocketChannel) next.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    try {
                        int readNum = channel.read(byteBuffer);
                        if (readNum == -1) {
                            System.out.println("读取到-1，表示连接结束，关闭 socket");
                            next.channel();
                            channel.close();
                            break;
                        }
                        byteBuffer.flip();
                        byte[] bytes =  new byte[readNum];
                        byteBuffer.get(bytes, 0, readNum);
                        System.out.println(new String(bytes));
                    } catch (IOException e) {
                        System.out.println("读取时发生异常，连接中断，关闭 socket");
                        next.channel();
                        channel.close();
                    }
                }
            }
        }
    }
}
