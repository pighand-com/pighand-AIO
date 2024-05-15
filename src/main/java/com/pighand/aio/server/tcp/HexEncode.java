package com.pighand.aio.server.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

@Slf4j
public class HexEncode extends MessageToByteEncoder<String> {
    /**
     * @param src 16进制字符串
     * @return 字节数组
     * @Title:hexString2Bytes
     * @Description:16进制字符串转字节数组
     */

    public static byte[] hexString2Bytes(String src) {
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {

            ret[i] = Integer.valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();

        }
        return ret;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String s, ByteBuf byteBuf) throws Exception {
        //将16进制字符串转为数组
        //        String hexString = string2HexString(s);
        //        byteBuf.writeBytes(hexString2Bytes(hexString));
        try {
            if (s.startsWith("A0")) {
                // 字符串使用utf8格式
                byteBuf.writeBytes(s.getBytes(StandardCharsets.UTF_8));
            } else {
                // 其他使用hex格式

                BigInteger decimal = new BigInteger(s, 16);

                // 将BigInteger转换为字节数组
                byte[] bytes = decimal.toByteArray();

                // 去除开头的额外零字节（如果有）
                if (bytes[0] == 0) {
                    byte[] temp = new byte[bytes.length - 1];
                    System.arraycopy(bytes, 1, temp, 0, temp.length);
                    bytes = temp;
                }
                byteBuf.writeBytes(bytes);
            }
        } catch (Exception e) {
            log.error("HexEncode encode error", e);
        }

    }

    public String utf82ASC(String s) {
        char[] chars = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char aChar : chars) {
            sb.append(Integer.toHexString(aChar));
        }
        return sb.toString();
    }

    public String string2HexString(String message) {
        byte[] bytes = message.getBytes();
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
