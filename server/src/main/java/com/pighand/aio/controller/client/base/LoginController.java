package com.pighand.aio.controller.client.base;

import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.common.interfaces.ApplicationId;
import com.pighand.aio.service.base.UserService;
import com.pighand.aio.service.base.tripartite.wechat.AppletImpl;
import com.pighand.aio.vo.base.Login;
import com.pighand.aio.vo.base.UserVO;
import com.pighand.framework.spring.api.annotation.Post;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 授权相关
 * 包含登录、三方平台信息获取
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@RequiredArgsConstructor
@RestController(docName = "客户端 - 登录", value = "client/login")
public class LoginController extends BaseController<UserService> {

    private final AppletImpl wechatAppletService;

    @ApplicationId
    @Post(value = "wechat/applet", docDescription = "微信小程序登录")
    public Result wechatAppletLogin(@RequestBody Login login) {
        Long applicationId = Context.applicationId();
        UserVO userVO = wechatAppletService.loginInByCode(applicationId, login.getCode());
        return new Result(userVO);
    }
}
