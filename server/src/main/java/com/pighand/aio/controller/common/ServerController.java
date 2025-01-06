package com.pighand.aio.controller.common;

import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.domain.IoT.DeviceDomain;
import com.pighand.aio.server.tcp.TcpClient;
import com.pighand.aio.service.IoT.DeviceService;
import com.pighand.aio.vo.base.LoginUser;
import com.pighand.framework.spring.api.annotation.Post;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@RestController(path = "server")
public class ServerController {

    private final DeviceService deviceService;

    @Post(path = "tcp/{id}", docSummary = "TCP连接，向客户端发送消息")
    public Result tcp(@PathVariable(name = "id") Long id, @RequestBody() String body) {
        LoginUser loginUserInfo = Context.loginUser();

        DeviceDomain deviceDomain = deviceService.find(id);

        TcpClient.sendMessage(deviceDomain.getClientId(), body);

        return new Result();
    }
}
