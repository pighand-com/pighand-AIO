package com.pighand.aio.controller.dashboard.base;

import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.common.interfaces.ApplicationId;
import com.pighand.aio.common.interfaces.Authorization;
import com.pighand.aio.domain.base.StoreDomain;
import com.pighand.aio.service.base.StoreService;
import com.pighand.aio.vo.base.StoreVO;
import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 门店
 *
 * @author wangshuli
 * @createDate 2024-12-31 19:04:50
 */
@Authorization
@ApplicationId
@RestController(path = "dashboard/store", docName = "门店")
public class StoreController extends BaseController<StoreService> {

    /**
     * @param baseStoreVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "baseStoreCreate")
    public Result<StoreVO> create(@Validated({ValidationGroup.Create.class}) @RequestBody StoreVO baseStoreVO) {
        baseStoreVO = super.service.create(baseStoreVO);

        return new Result(baseStoreVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<StoreDomain> find(@PathVariable(name = "id") Long id) {
        StoreDomain baseStoreDomain = super.service.find(id);

        return new Result(baseStoreDomain);
    }

    /**
     * @param baseStoreVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "baseStoreQuery")
    public Result<PageOrList<StoreVO>> query(StoreVO baseStoreVO) {
        Long storeId = Context.storeId();
        if (VerifyUtils.isNotEmpty(storeId)) {
            baseStoreVO.setId(storeId);
        }

        PageOrList<StoreVO> result = super.service.query(baseStoreVO);

        return new Result(result);
    }

    /**
     * @param baseStoreVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "baseStoreUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody StoreVO baseStoreVO) {
        baseStoreVO.setId(id);

        super.service.update(baseStoreVO);

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
