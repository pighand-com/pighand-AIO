package com.pighand.aio.controller.dashboard.distribution;

import com.pighand.aio.domain.distribution.DistributionSalesSettlementDomain;
import com.pighand.aio.service.distribution.DistributionSalesSettlementService;
import com.pighand.aio.vo.distribution.DistributionSalesSettlementVO;
import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 分销 - 结算记录
 *
 * @author wangshuli
 * @createDate 2025-05-24 14:35:05
 */
@RestController(path = "distribution/sales/settlement", docName = "分销 - 结算记录")
public class DistributionSalesSettlementController extends BaseController<DistributionSalesSettlementService> {

    /**
     * @param distDistributionSalesSettlementVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "distDistributionSalesSettlementCreate")
    public Result<DistributionSalesSettlementVO> create(@Validated({
        ValidationGroup.Create.class}) @RequestBody DistributionSalesSettlementVO distDistributionSalesSettlementVO) {
        distDistributionSalesSettlementVO = super.service.create(distDistributionSalesSettlementVO);

        return new Result(distDistributionSalesSettlementVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<DistributionSalesSettlementDomain> find(@PathVariable(name = "id") Long id) {
        DistributionSalesSettlementDomain distDistributionSalesSettlementDomain = super.service.find(id);

        return new Result(distDistributionSalesSettlementDomain);
    }

    /**
     * @param distDistributionSalesSettlementVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "distDistributionSalesSettlementQuery")
    public Result<PageOrList<DistributionSalesSettlementVO>> query(
        DistributionSalesSettlementVO distDistributionSalesSettlementVO) {
        PageOrList<DistributionSalesSettlementVO> result = super.service.query(distDistributionSalesSettlementVO);

        return new Result(result);
    }

    /**
     * @param distDistributionSalesSettlementVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "distDistributionSalesSettlementUpdate")
    public Result update(@PathVariable(name = "id") Long id,
        @RequestBody DistributionSalesSettlementVO distDistributionSalesSettlementVO) {
        distDistributionSalesSettlementVO.setId(id);

        super.service.update(distDistributionSalesSettlementVO);

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
