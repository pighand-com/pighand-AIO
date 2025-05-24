package com.pighand.aio.service.distribution;

import com.pighand.aio.domain.distribution.DistributionSalesDomain;
import com.pighand.aio.vo.distribution.DistributionSalesVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 分销 - 销售记录
 *
 * @author wangshuli
 * @createDate 2025-05-24 14:35:05
 */
public interface DistributionSalesService extends BaseService<DistributionSalesDomain> {

    /**
     * 创建
     *
     * @param distDistributionSalesVO
     * @return
     */
    DistributionSalesVO create(DistributionSalesVO distDistributionSalesVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    DistributionSalesDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param distDistributionSalesVO
     * @return PageOrList<DistDistributionSalesVO>
     */
    PageOrList<DistributionSalesVO> query(DistributionSalesVO distDistributionSalesVO);

    /**
     * 修改
     *
     * @param distDistributionSalesVO
     */
    void update(DistributionSalesVO distDistributionSalesVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}
