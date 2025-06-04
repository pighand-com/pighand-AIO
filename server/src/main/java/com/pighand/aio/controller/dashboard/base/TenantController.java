package com.pighand.aio.controller.dashboard.base;

import com.pighand.aio.common.interfaces.ApplicationId;
import com.pighand.aio.common.interfaces.Authorization;
import com.pighand.aio.domain.base.TenantDomain;
import com.pighand.aio.service.base.TenantService;
import com.pighand.aio.vo.base.TenantVO;
import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 租户
 *
 * @author wangshuli
 * @createDate 2024-12-31 19:04:50
 */
@Authorization
@ApplicationId
@RestController(path = "dashboard/tenant", docName = "租户")
public class TenantController extends BaseController<TenantService> {

    /**
     * @param baseTenantVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "baseTenantCreate")
    public Result<TenantVO> create(@Validated({ValidationGroup.Create.class}) @RequestBody TenantVO baseTenantVO) {
        baseTenantVO = super.service.create(baseTenantVO);

        return new Result(baseTenantVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<TenantDomain> find(@PathVariable(name = "id") Long id) {
        TenantDomain baseTenantDomain = super.service.find(id);

        return new Result(baseTenantDomain);
    }

    /**
     * @param baseTenantVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "baseTenantQuery")
    public Result<PageOrList<TenantVO>> query(TenantVO baseTenantVO) {
        PageOrList<TenantVO> result = super.service.query(baseTenantVO);

        return new Result(result);
    }

    /**
     * @param baseTenantVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "baseTenantUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody TenantVO baseTenantVO) {
        baseTenantVO.setId(id);

        super.service.update(baseTenantVO);

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
