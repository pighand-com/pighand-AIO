package com.pighand.aio.controller.ECommerce;

import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.domain.ECommerce.TicketDomain;
import com.pighand.aio.service.ECommerce.TicketService;
import com.pighand.aio.service.ECommerce.TicketUserService;
import com.pighand.aio.vo.ECommerce.TicketUserVO;
import com.pighand.aio.vo.ECommerce.TicketVO;
import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 电商 - 票务
 *
 * @author wangshuli
 * @createDate 2024-04-26 14:52:18
 */
//@Authorization
@RequiredArgsConstructor
@RestController(path = "ticket", docName = "电商 - 票务")
public class TicketController extends BaseController<TicketService> {

    private final TicketUserService ticketUserService;

    /**
     * @param ticketVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "ticketCreate")
    public Result<TicketVO> create(@Validated({ValidationGroup.Create.class}) @RequestBody TicketVO ticketVO) {
        ticketVO = super.service.create(ticketVO);

        return new Result(ticketVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<TicketDomain> find(@PathVariable(name = "id") Long id) {
        TicketDomain ticketDomain = super.service.find(id);

        return new Result(ticketDomain);
    }

    /**
     * @param ticketVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "ticketQuery")
    public Result<PageOrList<TicketVO>> query(TicketVO ticketVO) {
        PageOrList<TicketVO> result = super.service.query(ticketVO);

        return new Result(result);
    }

    /**
     * @param ticketVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "ticketUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody TicketVO ticketVO) {
        ticketVO.setId(id);

        // Prohibit modifying fields
        ticketVO.setId(null);
        ticketVO.setDeleted(null);

        super.service.update(ticketVO);

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

    /**
     * @param ticketUserVO
     */
    @Get(path = "user", docSummary = "我的票务")
    public Result<PageOrList<TicketUserVO>> query(TicketUserVO ticketUserVO) {
        ticketUserVO.setCreatorId(Context.loginUser().getId());
        PageOrList<TicketUserVO> result = ticketUserService.query(ticketUserVO);

        return new Result(result);
    }

    /**
     * @param ticketUserVO
     */
    @Post(path = "validation", docSummary = "票务核销")
    public Result validation(@RequestBody TicketUserVO ticketUserVO) {
        ticketUserService.validation(ticketUserVO);

        return new Result();
    }
}
