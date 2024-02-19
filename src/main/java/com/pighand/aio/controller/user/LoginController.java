package com.pighand.aio.controller.user;

import com.pighand.framework.spring.api.annotation.Post;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.response.Result;
import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.service.user.LoginService;
import com.pighand.aio.service.user.UserService;
import com.pighand.aio.service.user.tripartite.wechat.AppletImpl;
import com.pighand.aio.vo.user.Login;
import com.pighand.aio.vo.user.UserVO;
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
    public Result<UserVO> password(@RequestBody Login login) {
        UserVO user = loginService.byPassword(login.getUsername(), login.getPassword());
        return new Result(user);
    }

    @Post("wechat/applet")
    public Result wechatApplet(@RequestBody Login login) {
        Long projectId = Context.getProjectId();
        UserVO userVO = wechatAppletService.signInByCode(projectId, login.getCode());
        return new Result(userVO);
    }

}
