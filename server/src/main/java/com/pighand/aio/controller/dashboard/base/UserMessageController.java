package com.pighand.aio.controller.dashboard.base;

import com.pighand.aio.common.interfaces.Authorization;
import com.pighand.aio.service.base.UserMessageService;
import com.pighand.aio.vo.base.UserMessageVO;
import com.pighand.framework.spring.api.annotation.Delete;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.Post;
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
 * 用户 - 消息
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Authorization
@RequiredArgsConstructor
@RestController(path = "dashboard/message", docName = "用户 - 消息")
public class UserMessageController extends BaseController<UserMessageService> {

    @Post(docSummary = "发送消息", fieldGroup = "userMessageCreate")
    public Result<UserMessageVO> create(
        @Validated({ValidationGroup.Create.class}) @RequestBody UserMessageVO userMessageVO) {
        userMessageVO = super.service.create(userMessageVO);

        return new Result(userMessageVO);
    }

    @Get(path = "received", docSummary = "查询收到的", fieldGroup = "userMessageQuery")
    public Result<PageOrList<UserMessageVO>> queryReceived(UserMessageVO userMessageVO) {
        PageOrList<UserMessageVO> result = super.service.queryReceived(userMessageVO);

        return new Result(result);
    }

    @Get(path = "sent", docSummary = "查询发送的", fieldGroup = "userMessageQuery")
    public Result<PageOrList<UserMessageVO>> querySent(UserMessageVO userMessageVO) {
        PageOrList<UserMessageVO> result = super.service.querySent(userMessageVO);

        return new Result(result);
    }

    @Get(path = "{id}", docSummary = "详情")
    public Result<UserMessageVO> find(@PathVariable(name = "id") Long id) {
        UserMessageVO userMessageVO = super.service.find(id);

        return new Result(userMessageVO);
    }

    @Delete(path = "{id}", docSummary = "删除")
    public Result querySent(@PathVariable(name = "id") Long id) {
        super.service.delete(id);

        return new Result();
    }
}
