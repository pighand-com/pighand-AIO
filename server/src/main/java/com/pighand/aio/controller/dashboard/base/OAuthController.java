package com.pighand.aio.controller.dashboard.base;

import com.pighand.aio.common.CAPTCHA.CAPTCHA;
import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.common.interfaces.ApplicationId;
import com.pighand.aio.service.base.LoginService;
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
@RestController(docName = "授权相关。包含登录、三方平台信息获取")
public class OAuthController extends BaseController<UserService> {

    private final AppletImpl wechatAppletService;

    private final LoginService loginService;

    @Post(value = "login", docDescription = "密码登录")
    @CAPTCHA(key = "username")
    public Result<UserVO> passwordLogin(@RequestBody Login login) {
        UserVO user = loginService.loginByPassword(login.getUsername(), login.getPassword());
        return new Result(user);
    }

    @ApplicationId
    @Post(value = "login/wechat/applet", docDescription = "微信小程序登录")
    public Result wechatAppletLogin(@RequestBody Login login) {
        Long applicationId = Context.applicationId();
        UserVO userVO = wechatAppletService.loginInByCode(applicationId, login.getCode());
        return new Result(userVO);
    }
}
