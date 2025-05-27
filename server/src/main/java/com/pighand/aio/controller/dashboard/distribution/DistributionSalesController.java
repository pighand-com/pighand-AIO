package com.pighand.aio.controller.dashboard.distribution;

import com.pighand.aio.domain.distribution.DistributionSalesDomain;
import com.pighand.aio.service.distribution.DistributionSalesService;
import com.pighand.aio.vo.distribution.DistributionSalesVO;
import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 分销 - 销售记录
 *
 * @author wangshuli
 * @createDate 2025-05-24 14:35:05
 */
@RestController(path = "distribution/sales", docName = "分销 - 销售记录")
public class DistributionSalesController extends BaseController<DistributionSalesService> {

    /**
     * @param distDistributionSalesVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "distDistributionSalesCreate")
    public Result<DistributionSalesVO> create(
        @Validated({ValidationGroup.Create.class}) @RequestBody DistributionSalesVO distDistributionSalesVO) {
        distDistributionSalesVO = super.service.create(distDistributionSalesVO);

        return new Result(distDistributionSalesVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<DistributionSalesDomain> find(@PathVariable(name = "id") Long id) {
        DistributionSalesDomain distDistributionSalesDomain = super.service.find(id);

        return new Result(distDistributionSalesDomain);
    }

    /**
     * @param distDistributionSalesVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "distDistributionSalesQuery")
    public Result<PageOrList<DistributionSalesVO>> query(DistributionSalesVO distDistributionSalesVO) {
        PageOrList<DistributionSalesVO> result = super.service.query(distDistributionSalesVO);

        return new Result(result);
    }

    /**
     * @param distDistributionSalesVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "distDistributionSalesUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody DistributionSalesVO distDistributionSalesVO) {
        distDistributionSalesVO.setId(id);

        super.service.update(distDistributionSalesVO);

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
