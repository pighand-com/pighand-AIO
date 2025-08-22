package com.pighand.aio.controller.client.base;

import com.pighand.aio.common.interfaces.ApplicationId;
import com.pighand.aio.domain.base.StoreDomain;
import com.pighand.aio.service.base.StoreService;
import com.pighand.aio.vo.base.StoreVO;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 门店
 *
 * @author wangshuli
 * @createDate 2024-12-31 19:04:50
 */
@ApplicationId
@RestController(path = "client/store", docName = "门店")
public class StoreController extends BaseController<StoreService> {

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<StoreDomain> find(@PathVariable(name = "id") Long id) {
        StoreDomain baseStoreDomain = super.service.find(id);

        return new Result(baseStoreDomain);
    }

    @Get(docSummary = "列表")
    public Result<PageOrList<StoreVO>> query(StoreVO storeVO) {
        PageOrList<StoreVO> result = super.service.query(storeVO);

        return new Result(result);
    }
}
