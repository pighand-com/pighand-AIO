package com.pighand.aio.server.tcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TcpServerInitializer extends ChannelInitializer<SocketChannel> {

    private final TcpServerHandler tcpServerHandler;

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("decoder", new HexDecoder());
        pipeline.addLast("encoder", new HexEncode());
        pipeline.addLast(tcpServerHandler);
    }
}
