package com.pighand.aio.controller.dashboard.ECommerce;

import com.pighand.aio.common.interfaces.Authorization;
import com.pighand.aio.domain.ECommerce.SessionDomain;
import com.pighand.aio.service.ECommerce.SessionService;
import com.pighand.aio.service.ECommerce.SessionUserGroupService;
import com.pighand.aio.vo.ECommerce.SessionUserGroupVO;
import com.pighand.aio.vo.ECommerce.SessionVO;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.Post;
import com.pighand.framework.spring.api.annotation.Put;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 电商 - 场次
 *
 * @author wangshuli
 * @createDate 2023-12-05 16:13:27
 */
@RequiredArgsConstructor
@RestController(path = "session", docName = "电商 - 场次")
public class SessionController extends BaseController<SessionService> {

    private final SessionUserGroupService sessionUserGroupService;

    /**
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "sessionCreate")
    public Result<SessionVO> create() {
        SessionVO sessionVO = super.service.create(null);

        return new Result(sessionVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<SessionDomain> find(@PathVariable(name = "id") Long id) {
        SessionDomain sessionDomain = super.service.find(id);

        return new Result(sessionDomain);
    }

    /**
     * @param sessionVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "sessionQuery")
    public Result<PageOrList<SessionVO>> query(SessionVO sessionVO) {
        PageOrList<SessionVO> result = super.service.query(sessionVO);

        return new Result(result);
    }

    /**
     * @param sessionVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "sessionUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody SessionVO sessionVO) {
        sessionVO.setId(id);
        super.service.update(sessionVO);

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

    @Authorization()
    @Get(path = "wechat/applet/qrcode", docSummary = "场次微信小程序码")
    public Result<String> getWechatAppletQrcode(Long money) {
        String qrcode = sessionUserGroupService.getWechatAppletQrcode(money);

        return new Result(qrcode);
    }

    @Post(value = "join", docSummary = "加入场次")
    public Result<SessionUserGroupVO> join(@RequestBody SessionVO sessionVO) {
        SessionUserGroupVO sessionTemplateGourp = super.service.join(sessionVO.getId(), sessionVO.getSessionGroupId());
        return new Result(sessionTemplateGourp);
    }

}
