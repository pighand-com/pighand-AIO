package com.pighand.aio.service.distribution;

import com.pighand.aio.domain.distribution.DistributionSalesSettlementDomain;
import com.pighand.aio.vo.distribution.DistributionSalesSettlementVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 分销 - 结算记录
 *
 * @author wangshuli
 * @createDate 2025-05-24 14:35:05
 */
public interface DistributionSalesSettlementService extends BaseService<DistributionSalesSettlementDomain> {

    /**
     * 创建
     *
     * @param distDistributionSalesSettlementVO
     * @return
     */
    DistributionSalesSettlementVO create(DistributionSalesSettlementVO distDistributionSalesSettlementVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    DistributionSalesSettlementDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param distDistributionSalesSettlementVO
     * @return PageOrList<DistDistributionSalesSettlementVO>
     */
    PageOrList<DistributionSalesSettlementVO> query(DistributionSalesSettlementVO distDistributionSalesSettlementVO);

    /**
     * 修改
     *
     * @param distDistributionSalesSettlementVO
     */
    void update(DistributionSalesSettlementVO distDistributionSalesSettlementVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}
