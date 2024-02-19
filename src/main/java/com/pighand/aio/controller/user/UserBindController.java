package com.pighand.aio.controller.user;

import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import com.pighand.aio.domain.user.UserBindDomain;
import com.pighand.aio.service.user.UserBindService;
import com.pighand.aio.vo.user.UserBindVO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 用户 - 绑定信息
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@RequiredArgsConstructor
@RestController(path = "user/bind", docName = "用户 - 绑定信息")
public class UserBindController extends BaseController<UserBindService> {

    /**
     * @param userBindVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "userBindCreate")
    public Result<UserBindVO> create(@Validated({ValidationGroup.Create.class}) @RequestBody UserBindVO userBindVO) {
        userBindVO = super.service.create(userBindVO);

        return new Result(userBindVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<UserBindDomain> find(@PathVariable(name = "id") Long id) {
        UserBindDomain userBindDomain = super.service.find(id);

        return new Result(userBindDomain);
    }

    /**
     * @param userBindVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "userBindQuery")
    public Result<PageOrList<UserBindVO>> query(UserBindVO userBindVO) {
        PageOrList<UserBindVO> result = super.service.query(userBindVO);

        return new Result(result);
    }

    /**
     * @param userBindVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "userBindUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody UserBindVO userBindVO) {
        userBindVO.setId(id);
        super.service.update(userBindVO);

        return new Result();
    }

    /**
     * @param id
     */
    @Delete(path = "{id}", docSummary = "删除")
    public Result delete(@PathVariable(name = "id") Long id) {
        super.service.delete(id);
        return new Result();
    }
}
