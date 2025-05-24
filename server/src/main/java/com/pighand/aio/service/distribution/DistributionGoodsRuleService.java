package com.pighand.aio.service.distribution;

import com.pighand.aio.domain.distribution.DistributionGoodsRuleDomain;
import com.pighand.aio.vo.distribution.DistributionGoodsRuleVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 分销 - 分销商品规则
 *
 * @author wangshuli
 * @createDate 2025-05-24 14:35:05
 */
public interface DistributionGoodsRuleService extends BaseService<DistributionGoodsRuleDomain> {

    /**
     * 创建
     *
     * @param distDistributionGoodsRuleVO
     * @return
     */
    DistributionGoodsRuleVO create(DistributionGoodsRuleVO distDistributionGoodsRuleVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    DistributionGoodsRuleDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param distDistributionGoodsRuleVO
     * @return PageOrList<DistDistributionGoodsRuleVO>
     */
    PageOrList<DistributionGoodsRuleVO> query(DistributionGoodsRuleVO distDistributionGoodsRuleVO);

    /**
     * 修改
     *
     * @param distDistributionGoodsRuleVO
     */
    void update(DistributionGoodsRuleVO distDistributionGoodsRuleVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}
