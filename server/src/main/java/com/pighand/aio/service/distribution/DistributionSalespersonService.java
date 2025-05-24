package com.pighand.aio.service.distribution;

import com.pighand.aio.domain.distribution.DistributionSalespersonDomain;
import com.pighand.aio.vo.distribution.DistributionSalespersonVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 分销 - 销售资格
 *
 * @author wangshuli
 * @createDate 2025-05-24 14:35:05
 */
public interface DistributionSalespersonService extends BaseService<DistributionSalespersonDomain> {

    /**
     * 创建
     *
     * @param distDistributionSalespersonVO
     * @return
     */
    DistributionSalespersonVO create(DistributionSalespersonVO distDistributionSalespersonVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    DistributionSalespersonDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param distDistributionSalespersonVO
     * @return PageOrList<DistDistributionSalespersonVO>
     */
    PageOrList<DistributionSalespersonVO> query(DistributionSalespersonVO distDistributionSalespersonVO);

    /**
     * 修改
     *
     * @param distDistributionSalespersonVO
     */
    void update(DistributionSalespersonVO distDistributionSalespersonVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}
