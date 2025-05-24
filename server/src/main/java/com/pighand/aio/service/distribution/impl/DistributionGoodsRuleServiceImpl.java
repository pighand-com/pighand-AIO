package com.pighand.aio.service.distribution.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.pighand.aio.domain.distribution.DistributionGoodsRuleDomain;
import com.pighand.aio.mapper.distribution.DistributionGoodsRuleMapper;
import com.pighand.aio.service.distribution.DistributionGoodsRuleService;
import com.pighand.aio.vo.distribution.DistributionGoodsRuleVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.distribution.table.DistributionGoodsRuleTableDef.DISTRIBUTION_GOODS_RULE;

/**
 * 分销 - 分销商品规则
 *
 * @author wangshuli
 * @createDate 2025-05-24 14:35:05
 */
@Service
public class DistributionGoodsRuleServiceImpl
    extends BaseServiceImpl<DistributionGoodsRuleMapper, DistributionGoodsRuleDomain>
    implements DistributionGoodsRuleService {

    /**
     * 创建
     *
     * @param distDistributionGoodsRuleVO
     * @return
     */
    @Override
    public DistributionGoodsRuleVO create(DistributionGoodsRuleVO distDistributionGoodsRuleVO) {
        super.mapper.insert(distDistributionGoodsRuleVO);

        return distDistributionGoodsRuleVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public DistributionGoodsRuleDomain find(Long id) {
        return super.mapper.find(id, null);
    }

    /**
     * 分页或列表
     *
     * @param distDistributionGoodsRuleVO
     * @return PageOrList<DistDistributionGoodsRuleVO>
     */
    @Override
    public PageOrList<DistributionGoodsRuleVO> query(DistributionGoodsRuleVO distDistributionGoodsRuleVO) {

        QueryWrapper queryWrapper = QueryWrapper.create()

            // like
            .and(DISTRIBUTION_GOODS_RULE.OBJECT_TYPE.like(distDistributionGoodsRuleVO.getObjectType()))

            // equal
            .and(DISTRIBUTION_GOODS_RULE.OBJECT_ID.eq(distDistributionGoodsRuleVO.getObjectId()))
            .and(DISTRIBUTION_GOODS_RULE.SHARING_TYPE.eq(distDistributionGoodsRuleVO.getSharingType()))
            .and(DISTRIBUTION_GOODS_RULE.SHARING_RATIO.eq(distDistributionGoodsRuleVO.getSharingRatio()))
            .and(DISTRIBUTION_GOODS_RULE.SHARING_PRICE.eq(distDistributionGoodsRuleVO.getSharingPrice()));

        return super.mapper.query(distDistributionGoodsRuleVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param distDistributionGoodsRuleVO
     */
    @Override
    public void update(DistributionGoodsRuleVO distDistributionGoodsRuleVO) {
        UpdateChain updateChain =
            this.updateChain().where(DISTRIBUTION_GOODS_RULE.ID.eq(distDistributionGoodsRuleVO.getId()));

        updateChain.set(DISTRIBUTION_GOODS_RULE.ID, distDistributionGoodsRuleVO.getId(), VerifyUtils::isNotEmpty);

        updateChain.update();
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        super.mapper.deleteById(id);
    }
}
