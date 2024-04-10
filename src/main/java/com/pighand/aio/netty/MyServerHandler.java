package com.pighand.aio.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 处理接收到的消息
        ByteBuf buf = Unpooled.copiedBuffer(msg.toString().getBytes());
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        String message = new String(data);
        System.out.println("Received message from client: " + message);

        // 这里可以根据需要进行业务逻辑处理

        // 响应客户端
        ctx.writeAndFlush("\ntime: " + System.currentTimeMillis() + "\n" + "message: \n" + msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 异常处理
        cause.printStackTrace();
        ctx.close();
    }
}
