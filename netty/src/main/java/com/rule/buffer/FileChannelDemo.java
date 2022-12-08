package com.rule.buffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelDemo {

    public static void main(String[] args) throws Exception{
        // 初始化：
        FileInputStream inputStream = new FileInputStream(new File("C:\\Users\\rulelau\\Desktop\\a.txt"));
        FileChannel fileChannel = inputStream.getChannel();
        // 读取文件内容：
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int num = fileChannel.read(buffer);
//        buffer.flip();
        System.out.println(print(buffer));
        // 写入文件内容：通过 FileInputStream 获取的 channel 只能读，如果使用写入write方法，会抛出异常：
//        buffer.put("随机写入一些内容到 Buffer 中".getBytes());
//        // Buffer 切换为读模式
//        buffer.flip();
//        while (buffer.hasRemaining()) {
//            // 将 Buffer 中的内容写入文件
//            fileChannel.write(buffer);
//        }
        writeFile();
        buffer.clear();
        fileChannel.read(buffer);
        System.out.println(print(buffer));
    }

    static void writeFile() throws Exception{
        FileOutputStream fileOutputStream = new FileOutputStream(new File("C:\\Users\\rulelau\\Desktop\\a.txt"),true);
        FileChannel fileChannel = fileOutputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
         buffer.put("随机写入一些内容到 Buffer 中".getBytes());
        // Buffer 切换为读模式
        buffer.flip();
        while (buffer.hasRemaining()) {
            // 将 Buffer 中的内容写入文件
            fileChannel.write(buffer);
        }
    }

    static String print(ByteBuffer b) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < b.limit(); i++) {
            stringBuilder.append((char) b.get(i));
        }
        return stringBuilder.toString();
    }
}
