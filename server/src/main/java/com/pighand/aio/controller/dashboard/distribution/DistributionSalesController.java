package com.pighand.aio.controller.dashboard.distribution;

import com.pighand.aio.service.distribution.DistributionSalesService;
import com.pighand.aio.vo.distribution.DistributionSalesVO;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.Post;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 分销 - 销售记录
 *
 * @author wangshuli
 * @createDate 2025-05-24 14:35:05
 */
@RestController(path = "dashboard/distribution/sales", docName = "分销 - 销售记录")
public class DistributionSalesController extends BaseController<DistributionSalesService> {

    /**
     * @param distDistributionSalesVO
     * @return
     */
    @Post(docSummary = "结算", fieldGroup = "distDistributionSalesCreate")
    public Result<DistributionSalesVO> create(
        @Validated({ValidationGroup.Create.class}) @RequestBody DistributionSalesVO distDistributionSalesVO) {
        distDistributionSalesVO.setType(20);

        if (distDistributionSalesVO.getSettledAmount().compareTo(BigDecimal.ZERO) != -1) {
            throw new IllegalArgumentException("金额必须小于0");
        }

        if (distDistributionSalesVO.getSettledDetailIds().isEmpty()) {
            throw new IllegalArgumentException("结算详情ID不能为空");
        }

        distDistributionSalesVO = super.service.create(distDistributionSalesVO);

        return new Result(distDistributionSalesVO);
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
     * 根据结算ID统计各个状态的总数
     *
     * @return
     */
    @Get(path = "statistics/status", docSummary = "根据结算ID统计各个状态的总数")
    public Result<Map<String, Object>> countBySettlementIdGroupByStatus(DistributionSalesVO distDistributionSalesVO) {
        Map<String, Object> statistics = super.service.statistics(distDistributionSalesVO);
        return new Result<>(statistics);
    }
}
