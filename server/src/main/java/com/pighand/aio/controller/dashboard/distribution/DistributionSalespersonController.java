package com.pighand.aio.controller.dashboard.distribution;

import com.pighand.aio.domain.distribution.DistributionSalespersonDomain;
import com.pighand.aio.service.distribution.DistributionSalespersonService;
import com.pighand.aio.vo.distribution.DistributionSalespersonVO;
import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 分销 - 销售资格
 *
 * @author wangshuli
 * @createDate 2025-05-24 14:35:05
 */
@RestController(path = "distribution/salesperson", docName = "分销 - 销售资格")
public class DistributionSalespersonController extends BaseController<DistributionSalespersonService> {

    /**
     * @param distDistributionSalespersonVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "distDistributionSalespersonCreate")
    public Result<DistributionSalespersonVO> create(@Validated({
        ValidationGroup.Create.class}) @RequestBody DistributionSalespersonVO distDistributionSalespersonVO) {
        distDistributionSalespersonVO = super.service.create(distDistributionSalespersonVO);

        return new Result(distDistributionSalespersonVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<DistributionSalespersonDomain> find(@PathVariable(name = "id") Long id) {
        DistributionSalespersonDomain distDistributionSalespersonDomain = super.service.find(id);

        return new Result(distDistributionSalespersonDomain);
    }

    /**
     * @param distDistributionSalespersonVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "distDistributionSalespersonQuery")
    public Result<PageOrList<DistributionSalespersonVO>> query(
        DistributionSalespersonVO distDistributionSalespersonVO) {
        PageOrList<DistributionSalespersonVO> result = super.service.query(distDistributionSalespersonVO);

        return new Result(result);
    }

    /**
     * @param distDistributionSalespersonVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "distDistributionSalespersonUpdate")
    public Result update(@PathVariable(name = "id") Long id,
        @RequestBody DistributionSalespersonVO distDistributionSalespersonVO) {
        distDistributionSalespersonVO.setId(id);

        super.service.update(distDistributionSalespersonVO);

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
