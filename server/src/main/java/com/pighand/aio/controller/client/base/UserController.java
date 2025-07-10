package com.pighand.aio.controller.client.base;

import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.common.interfaces.Authorization;
import com.pighand.aio.service.base.UserService;
import com.pighand.aio.service.base.tripartite.wechat.AppletImpl;
import com.pighand.aio.vo.base.LoginUser;
import com.pighand.aio.vo.base.UserVO;
import com.pighand.aio.vo.base.tripartite.EncryptedData;
import com.pighand.framework.spring.api.annotation.Post;
import com.pighand.framework.spring.api.annotation.Put;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 用户关键信息表，主要保存登录所用信息
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@RequiredArgsConstructor
@RestController(path = "client/user", docName = "用户关键信息表，主要保存登录所用信息")
public class UserController extends BaseController<UserService> {

    private final AppletImpl wechatAppletService;

    /**
     * @param userVO
     */
    @Authorization
    @Put(path = "self", docSummary = "修改自己的信息")
    public Result updateBySelf(@RequestBody UserVO userVO) {
        LoginUser longUser = Context.loginUser();
        userVO.setId(longUser.getId());
        super.service.update(userVO);

        return new Result();
    }

    /**
     * 绑定手机
     *
     * @param encryptedData
     * @return
     */
    @Authorization
    @Post(value = "bind/phone/wechat", docSummary = "绑定手机号")
    public Result<UserVO> bindPhone(@RequestBody EncryptedData encryptedData) {
        Long applicationId = Context.applicationId();
        LoginUser longUser = Context.loginUser();
        String phone = wechatAppletService.bindPhone(applicationId, longUser.getId(), encryptedData.getCode());

        UserVO userVO = new UserVO();
        userVO.setPhone(phone);
        return new Result(userVO);
    }
}
