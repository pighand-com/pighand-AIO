package com.pighand.aio.server.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TcpServer {
    private final TcpServerInitializer tcpServerInitializer;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    @Value("${server.tcp.port}")
    private int port;

    public TcpServer(TcpServerInitializer tcpServerInitializer) {
        this.tcpServerInitializer = tcpServerInitializer;
    }

    @PostConstruct
    public void start() throws Exception {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
            .childHandler(tcpServerInitializer);

        serverBootstrap.bind(port).sync();
        log.info("TCP Server started on port {}.", port);
    }

    @PreDestroy
    public void stop() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        System.out.println("TCP Server stopped.");
    }
}
