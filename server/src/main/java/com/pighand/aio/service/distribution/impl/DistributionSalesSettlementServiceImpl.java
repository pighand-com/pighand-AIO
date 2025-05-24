package com.pighand.aio.service.distribution.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.pighand.aio.domain.distribution.DistributionSalesSettlementDomain;
import com.pighand.aio.mapper.distribution.DistributionSalesSettlementMapper;
import com.pighand.aio.service.distribution.DistributionSalesSettlementService;
import com.pighand.aio.vo.distribution.DistributionSalesSettlementVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.distribution.table.DistributionSalesSettlementTableDef.DISTRIBUTION_SALES_SETTLEMENT;

/**
 * 分销 - 结算记录
 *
 * @author wangshuli
 * @createDate 2025-05-24 14:35:05
 */
@Service
public class DistributionSalesSettlementServiceImpl
    extends BaseServiceImpl<DistributionSalesSettlementMapper, DistributionSalesSettlementDomain>
    implements DistributionSalesSettlementService {

    /**
     * 创建
     *
     * @param distDistributionSalesSettlementVO
     * @return
     */
    @Override
    public DistributionSalesSettlementVO create(DistributionSalesSettlementVO distDistributionSalesSettlementVO) {
        super.mapper.insert(distDistributionSalesSettlementVO);

        return distDistributionSalesSettlementVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public DistributionSalesSettlementDomain find(Long id) {
        return super.mapper.find(id, null);
    }

    /**
     * 分页或列表
     *
     * @param distDistributionSalesSettlementVO
     * @return PageOrList<DistDistributionSalesSettlementVO>
     */
    @Override
    public PageOrList<DistributionSalesSettlementVO> query(
        DistributionSalesSettlementVO distDistributionSalesSettlementVO) {

        QueryWrapper queryWrapper = QueryWrapper.create()

            // equal
            .and(DISTRIBUTION_SALES_SETTLEMENT.AMOUNT.eq(distDistributionSalesSettlementVO.getAmount()));

        return super.mapper.query(distDistributionSalesSettlementVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param distDistributionSalesSettlementVO
     */
    @Override
    public void update(DistributionSalesSettlementVO distDistributionSalesSettlementVO) {
        UpdateChain updateChain =
            this.updateChain().where(DISTRIBUTION_SALES_SETTLEMENT.ID.eq(distDistributionSalesSettlementVO.getId()));

        updateChain.set(DISTRIBUTION_SALES_SETTLEMENT.ID, distDistributionSalesSettlementVO.getId(),
            VerifyUtils::isNotEmpty);
        updateChain.set(DISTRIBUTION_SALES_SETTLEMENT.CREATED_AT, distDistributionSalesSettlementVO.getCreatedAt(),
            VerifyUtils::isNotEmpty);

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
