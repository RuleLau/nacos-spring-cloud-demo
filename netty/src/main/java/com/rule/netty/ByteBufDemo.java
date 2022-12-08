package com.rule.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;

public class ByteBufDemo {

    public static void main(String[] args) {
        // 堆内，byte[] 数组
        ByteBuf b1 = UnpooledByteBufAllocator.DEFAULT.heapBuffer(8192);
        // 堆外内存，内存地址
        ByteBuf b2 = UnpooledByteBufAllocator.DEFAULT.directBuffer(8192);

        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.heapBuffer(1024);
        System.out.println(byteBuf.readerIndex() + ":" + byteBuf.writerIndex());
        byteBuf.writeByte(1);
        byteBuf.writeByte(1);
        byteBuf.writeByte(1);
        byteBuf.writeByte(1);
        System.out.println(byteBuf.readerIndex() + ":" + byteBuf.writerIndex());

        byteBuf.readByte();
        byteBuf.readByte();
        byteBuf.readByte();
        System.out.println(byteBuf.readerIndex() + ":" + byteBuf.writerIndex());
    }
}
