package com.pighand.aio.server.tcp;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * TCP客户端
 */
@Slf4j
public class TcpClient {
    private static final ConcurrentHashMap<String, ChannelHandlerContext> clientMap = new ConcurrentHashMap<>();

    public static void addClient(String clientId, ChannelHandlerContext ctx) {
        clientMap.put(clientId, ctx);
    }

    public static void removeClient(String clientId, ChannelHandlerContext ctx) {
        clientMap.remove(clientId);
    }

    public static void sendMessage(String clientId, Object message) {
        log.info("TcpClient.sendMessage(" + clientId + "): " + message);
        ChannelHandlerContext ctx = clientMap.get(clientId);
        if (ctx != null) {
            try {
                ctx.writeAndFlush(message);
            } catch (Exception e) {
                log.error("TcpClient.sendMessage(" + clientId + "): " + e.getMessage());
            }
        } else {
            log.error("TcpClient.sendMessage(" + clientId + "): ctx is null");
        }
    }
}
