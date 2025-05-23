package com.pighand.aio.controller.base;

import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.common.interfaces.Authorization;
import com.pighand.aio.service.base.UserService;
import com.pighand.aio.service.base.tripartite.wechat.AppletImpl;
import com.pighand.aio.vo.base.LoginUser;
import com.pighand.aio.vo.base.UserVO;
import com.pighand.aio.vo.base.tripartite.EncryptedData;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.Post;
import com.pighand.framework.spring.api.annotation.Put;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 用户关键信息表，主要保存登录所用信息
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@RequiredArgsConstructor
@RestController(path = "user", docName = "用户关键信息表，主要保存登录所用信息")
public class UserController extends BaseController<UserService> {

    private final AppletImpl wechatAppletService;

    /**
     * @param userVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "userCreate")
    public Result<UserVO> create(@Validated({ValidationGroup.Create.class}) @RequestBody UserVO userVO) {
        userVO = super.service.create(userVO);

        return new Result(userVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<UserVO> find(@PathVariable(name = "id") Long id) {
        UserVO userDomain = super.service.find(id);

        return new Result(userDomain);
    }

    /**
     * @param userVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "userQuery")
    public Result<PageOrList<UserVO>> query(UserVO userVO) {
        PageOrList<UserVO> result = super.service.query(userVO);

        return new Result(result);
    }

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
     * @param userVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "userUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody UserVO userVO) {
        userVO.setId(id);
        super.service.update(userVO);

        return new Result();
    }

    /**
     * @param id
     */
    @com.pighand.framework.spring.api.annotation.Delete(path = "{id}", docSummary = "删除")
    public Result delete(@PathVariable(name = "id") Long id) {
        super.service.delete(id);
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
