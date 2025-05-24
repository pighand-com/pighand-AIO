package com.pighand.aio.service.distribution.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.pighand.aio.domain.distribution.DistributionSalesDomain;
import com.pighand.aio.mapper.distribution.DistributionSalesMapper;
import com.pighand.aio.service.distribution.DistributionSalesService;
import com.pighand.aio.vo.distribution.DistributionSalesVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.distribution.table.DistributionSalesTableDef.DISTRIBUTION_SALES;

/**
 * 分销 - 销售记录
 *
 * @author wangshuli
 * @createDate 2025-05-24 14:35:05
 */
@Service
public class DistributionSalesServiceImpl extends BaseServiceImpl<DistributionSalesMapper, DistributionSalesDomain>
    implements DistributionSalesService {

    /**
     * 创建
     *
     * @param distDistributionSalesVO
     * @return
     */
    @Override
    public DistributionSalesVO create(DistributionSalesVO distDistributionSalesVO) {
        super.mapper.insert(distDistributionSalesVO);

        return distDistributionSalesVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public DistributionSalesDomain find(Long id) {
        return super.mapper.find(id, null);
    }

    /**
     * 分页或列表
     *
     * @param distDistributionSalesVO
     * @return PageOrList<DistDistributionSalesVO>
     */
    @Override
    public PageOrList<DistributionSalesVO> query(DistributionSalesVO distDistributionSalesVO) {

        QueryWrapper queryWrapper = QueryWrapper.create()

            // equal
            .and(DISTRIBUTION_SALES.USER_ID.eq(distDistributionSalesVO.getUserId()))
            .and(DISTRIBUTION_SALES.ORDER_ID.eq(distDistributionSalesVO.getOrderId()))
            .and(DISTRIBUTION_SALES.SETTLEMENT_ID.eq(distDistributionSalesVO.getSettlementId()))
            .and(DISTRIBUTION_SALES.AMOUNT.eq(distDistributionSalesVO.getAmount()))
            .and(DISTRIBUTION_SALES.TYPE.eq(distDistributionSalesVO.getType()))
            .and(DISTRIBUTION_SALES.STATUS.eq(distDistributionSalesVO.getStatus()));

        return super.mapper.query(distDistributionSalesVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param distDistributionSalesVO
     */
    @Override
    public void update(DistributionSalesVO distDistributionSalesVO) {
        UpdateChain updateChain = this.updateChain().where(DISTRIBUTION_SALES.ID.eq(distDistributionSalesVO.getId()));

        updateChain.set(DISTRIBUTION_SALES.ID, distDistributionSalesVO.getId(), VerifyUtils::isNotEmpty);

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
