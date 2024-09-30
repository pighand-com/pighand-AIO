package com.pighand.aio.server.tcp;

import com.pighand.aio.service.IoT.DeviceService;
import com.pighand.aio.service.IoT.DeviceTaskService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@ChannelHandler.Sharable
@AllArgsConstructor
public class TcpServerHandler extends ChannelInboundHandlerAdapter {
    private final DeviceService deviceService;
    private final DeviceTaskService deviceTaskService;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 处理接收到的消息
        log.info("Received message from client(org): {}", msg);
        String message = "";
        if (msg instanceof ByteBuf byteBuf) {
            message = byteBuf.toString(CharsetUtil.UTF_8);
        } else {
            ByteBuf buf = Unpooled.copiedBuffer(msg.toString().getBytes());
            byte[] data = new byte[buf.readableBytes()];
            buf.readBytes(data);
            message = new String(data);
        }
        log.info("Received message from client(decode): {}", message);

        // 缓存客户端
        String clientId = ctx.channel().id().toString();
        TcpClient.addClient(clientId, ctx);

        // 更新库中设备状态（心跳）
        String pattern = "AABBCC([a-zA-Z0-9]{4})0200FF";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(message);
        if (m.find()) {
            String sn = m.group(1);
            deviceService.link(sn, clientId);
        }

        // 付费播放成功（AABBCC+4位字母或数字+0302+2位数字+FF）
        pattern = "AABBCC([a-zA-Z0-9]{4})0302[0-9]{2}FF";
        r = Pattern.compile(pattern);
        m = r.matcher(message);
        if (m.find()) {
            String sn = m.group(1);
            deviceService.leisure(sn);
        }

        // 答题播放成功（AABBCC+4位字母或数字+0303+2位数字+FF）
        pattern = "AABBCC([a-zA-Z0-9]{4})0303[0-9]{2}FF";
        r = Pattern.compile(pattern);
        m = r.matcher(message);
        if (m.find()) {
            String sn = m.group(1);
            deviceService.leisure(sn);
        }

        // 超时未刷卡（AABBCC+4位字母或数字+0303FFFF）
        pattern = "AABBCC([a-zA-Z0-9]{4})0303FFFF";
        r = Pattern.compile(pattern);
        m = r.matcher(message);
        if (m.find()) {
            String sn = m.group(1);
            deviceService.leisure(sn);
        }

        // 播放结束（AABBCC+4位字母或数字+0304+2位数字+FF）
        pattern = "AABBCC([a-zA-Z0-9]{4})0304[0-9]{2}FF";
        r = Pattern.compile(pattern);
        m = r.matcher(message);
        if (m.find()) {
            String sn = m.group(1);
            deviceService.leisure(sn);
        }

        // 响应客户端
        //        TcpClient.sendMessage(clientId, msg);
    }

    /**
     * 异常处理
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        String id = ctx.channel().id().toString();
        log.error("TCP client error: {}, {}", id, cause.getMessage());
        ctx.close();

        deviceService.unlink(id);
    }

    /**
     * TCP连接关闭
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String id = ctx.channel().id().toString();
        log.info("TCP connection closed: {}", id);
        super.channelInactive(ctx);
        deviceService.unlink(id);
    }
}
