package com.pighand.aio.controller.dashboard.CMS;

import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.domain.CMS.BannerDomain;
import com.pighand.aio.service.CMS.BannerService;
import com.pighand.aio.vo.CMS.BannerVO;
import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * CMS - banner
 *
 * @author wangshuli
 * @createDate 2025-06-17 13:59:52
 */
@RestController(path = "dashboard/banner", docName = "CMS - banner")
public class BannerController extends BaseController<BannerService> {

    /**
     * @param cmsBannerVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "cmsBannerCreate")
    public Result<BannerVO> create(@Validated({ValidationGroup.Create.class}) @RequestBody BannerVO cmsBannerVO) {
        Long applicationId = Context.applicationId();

        cmsBannerVO.setApplicationId(applicationId);

        cmsBannerVO = super.service.create(cmsBannerVO);

        return new Result(cmsBannerVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<BannerDomain> find(@PathVariable(name = "id") Long id) {
        BannerDomain cmsBannerDomain = super.service.find(id);

        return new Result(cmsBannerDomain);
    }

    /**
     * @param cmsBannerVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "cmsBannerQuery")
    public Result<PageOrList<BannerVO>> query(BannerVO cmsBannerVO) {
        PageOrList<BannerVO> result = super.service.query(cmsBannerVO);

        return new Result(result);
    }

    /**
     * @param cmsBannerVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "cmsBannerUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody BannerVO cmsBannerVO) {
        cmsBannerVO.setId(id);

        super.service.update(cmsBannerVO);

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
     * @param id
     */
    @Put(path = "{id}/online", docSummary = "上架")
    public Result online(@PathVariable(name = "id") Long id) {
        super.service.online(id);
        return new Result();
    }

    /**
     * @param id
     */
    @Put(path = "{id}/offline", docSummary = "下架")
    public Result offline(@PathVariable(name = "id") Long id) {
        super.service.offline(id);
        return new Result();
    }
}
