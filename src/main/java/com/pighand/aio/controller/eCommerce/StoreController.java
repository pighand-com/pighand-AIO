package com.pighand.aio.controller.ECommerce;

import com.pighand.aio.domain.ECommerce.StoreDomain;
import com.pighand.aio.service.ECommerce.StoreService;
import com.pighand.aio.vo.ECommerce.StoreVO;
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
 * 电商 - 门店
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@RequiredArgsConstructor
@RestController(path = "store", docName = "电商 - 门店")
public class StoreController extends BaseController<StoreService> {

    /**
     * @param storeVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "storeCreate")
    public Result<StoreVO> create(@Validated({ValidationGroup.Create.class}) @RequestBody StoreVO storeVO) {
        storeVO = super.service.create(storeVO);

        return new Result(storeVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<StoreDomain> find(@PathVariable(name = "id") Long id) {
        StoreDomain storeDomain = super.service.find(id);

        return new Result(storeDomain);
    }

    /**
     * @param storeVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "storeQuery")
    public Result<PageOrList<StoreVO>> query(StoreVO storeVO) {
        PageOrList<StoreVO> result = super.service.query(storeVO);

        return new Result(result);
    }

    /**
     * @param storeVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "storeUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody StoreVO storeVO) {
        storeVO.setId(id);
        super.service.update(storeVO);

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
