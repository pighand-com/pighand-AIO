package com.pighand.aio.controller.dashboard.base;

import com.pighand.aio.common.CAPTCHA.CAPTCHA;
import com.pighand.aio.service.base.LoginService;
import com.pighand.aio.service.base.UserService;
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
@RestController(docName = "后台 - 登录", value = "dashboard/login")
public class LoginController extends BaseController<UserService> {

    private final LoginService loginService;

    @Post(docDescription = "密码登录")
    @CAPTCHA(key = "username")
    public Result<UserVO> passwordLogin(@RequestBody Login login) {
        UserVO user = loginService.loginByPassword(login.getUsername(), login.getPassword());
        return new Result(user);
    }
}
