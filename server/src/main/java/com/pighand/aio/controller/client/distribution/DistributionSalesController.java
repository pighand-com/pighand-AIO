package com.pighand.aio.controller.client.distribution;

import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.domain.distribution.DistributionSalesDetailDomain;
import com.pighand.aio.service.distribution.DistributionSalesService;
import com.pighand.aio.service.distribution.DistributionSalespersonService;
import com.pighand.aio.vo.distribution.DistributionSalesVO;
import com.pighand.aio.vo.distribution.DistributionSalespersonVO;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

/**
 * 分销 - 销售记录
 *
 * @author wangshuli
 * @createDate 2025-05-24 14:35:05
 */
@RestController(path = "client/distribution/sales", docName = "分销 - 销售记录")
@RequiredArgsConstructor
public class DistributionSalesController extends BaseController<DistributionSalesService> {

    private final DistributionSalespersonService distributionSalespersonService;

    /**
     * @param distDistributionSalesVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "distDistributionSalesQuery")
    public Result<PageOrList<DistributionSalesVO>> query(DistributionSalesVO distDistributionSalesVO) {
        Long loginUserId = Context.loginUser().getId();

        DistributionSalespersonVO distributionSalespersonVO = distributionSalespersonService.findByUserId(loginUserId);

        if (distributionSalespersonVO == null) {
            return new Result<>().prompt("无分销资格");
        }

        distDistributionSalesVO.setSalespersonId(distributionSalespersonVO.getId());
        PageOrList<DistributionSalesVO> result = super.service.query(distDistributionSalesVO);

        return new Result(result);
    }

    @Get(value = "{id}/detail", docSummary = "明细列表")
    public Result<List<DistributionSalesDetailDomain>> queryDetail(@PathVariable(name = "id") Long id) {
        List<DistributionSalesDetailDomain> result = super.service.queryDetail(id);

        return new Result(result);
    }

    /**
     * 根据结算ID统计各个状态的总数
     *
     * @return
     */
    @Get(path = "statistics", docSummary = "根据结算ID统计各个状态的总数")
    public Result<Map<String, Object>> countBySettlementIdGroupByStatus() {
        Long loginUserId = Context.loginUser().getId();
        DistributionSalesVO distDistributionSalesVO = new DistributionSalesVO();
        distDistributionSalesVO.setUserId(loginUserId);

        Map<String, Object> statistics = super.service.statistics(distDistributionSalesVO);
        return new Result<>(statistics);
    }
}
