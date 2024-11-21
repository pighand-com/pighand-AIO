package com.pighand.aio.controller.base;

import com.pighand.aio.common.CAPTCHA.CAPTCHA;
import com.pighand.aio.common.interceptor.Context;
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
 * 登录
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@RequiredArgsConstructor
@RestController(path = "login", docName = "用户关键信息表，主要保存登录所用信息")
public class LoginController extends BaseController<UserService> {

    private final AppletImpl wechatAppletService;
    private final LoginService loginService;

    @Post()
    @CAPTCHA(key = "username")
    public Result<UserVO> password(@RequestBody Login login) {
        UserVO user = loginService.byPassword(login.getUsername(), login.getPassword());
        return new Result(user);
    }

    @Post("wechat/applet")
    public Result wechatApplet(@RequestBody Login login) {
        Long applicationId = Context.getApplicationId();
        UserVO userVO = wechatAppletService.signInByCode(applicationId, login.getCode());
        return new Result(userVO);
    }

}
