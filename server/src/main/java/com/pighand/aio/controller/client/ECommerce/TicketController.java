package com.pighand.aio.controller.client.ECommerce;

import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.common.interfaces.Authorization;
import com.pighand.aio.domain.ECommerce.TicketDomain;
import com.pighand.aio.service.ECommerce.TicketService;
import com.pighand.aio.service.ECommerce.TicketUserService;
import com.pighand.aio.vo.ECommerce.TicketUserVO;
import com.pighand.aio.vo.ECommerce.TicketVO;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.Post;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 电商 - 票务
 *
 * @author wangshuli
 * @createDate 2024-04-26 14:52:18
 */
@RequiredArgsConstructor
@RestController(path = "client/ticket", docName = "电商 - 票务")
public class TicketController extends BaseController<TicketService> {

    private final TicketUserService ticketUserService;

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<TicketDomain> find(@PathVariable(name = "id") Long id) {
        TicketDomain ticketDomain = super.service.find(id);

        return new Result(ticketDomain);
    }

    @Get(docSummary = "票务列表")
    public Result<PageOrList<TicketVO>> query(TicketVO ticketVO) {
        PageOrList<TicketVO> result = super.service.query(ticketVO);

        return new Result(result);
    }

    @Authorization()
    @Get(docSummary = "我的列表", value = "mine")
    public Result<PageOrList<TicketUserVO>> queryMine(TicketUserVO ticketUserVO) {
        ticketUserVO.setCreatorId(Context.loginUser().getId());
        PageOrList<TicketUserVO> result = ticketUserService.query(ticketUserVO);

        return new Result(result);
    }

    // TODO 移动端使用id核销不安全，改为生成加密token（包含id和有效时间）
    @Authorization()
    @Post(path = "user/validation", docSummary = "票务核销")
    public Result validation(@RequestBody List<TicketUserVO> tickets) {
        ticketUserService.validation(tickets);

        return new Result();
    }
}
