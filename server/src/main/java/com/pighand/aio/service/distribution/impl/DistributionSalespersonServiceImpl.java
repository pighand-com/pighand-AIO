package com.pighand.aio.service.distribution.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.pighand.aio.domain.distribution.DistributionSalespersonDomain;
import com.pighand.aio.mapper.distribution.DistributionSalespersonMapper;
import com.pighand.aio.service.distribution.DistributionSalespersonService;
import com.pighand.aio.vo.distribution.DistributionSalespersonVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.distribution.table.DistributionSalespersonTableDef.DISTRIBUTION_SALESPERSON;

/**
 * 分销 - 销售资格
 *
 * @author wangshuli
 * @createDate 2025-05-24 14:35:05
 */
@Service
public class DistributionSalespersonServiceImpl
    extends BaseServiceImpl<DistributionSalespersonMapper, DistributionSalespersonDomain>
    implements DistributionSalespersonService {

    /**
     * 创建
     *
     * @param distDistributionSalespersonVO
     * @return
     */
    @Override
    public DistributionSalespersonVO create(DistributionSalespersonVO distDistributionSalespersonVO) {
        super.mapper.insert(distDistributionSalespersonVO);

        return distDistributionSalespersonVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public DistributionSalespersonDomain find(Long id) {
        return super.mapper.find(id, null);
    }

    /**
     * 分页或列表
     *
     * @param distDistributionSalespersonVO
     * @return PageOrList<DistDistributionSalespersonVO>
     */
    @Override
    public PageOrList<DistributionSalespersonVO> query(DistributionSalespersonVO distDistributionSalespersonVO) {

        QueryWrapper queryWrapper = QueryWrapper.create()

            // equal
            .and(DISTRIBUTION_SALESPERSON.USER_ID.eq(distDistributionSalespersonVO.getUserId()))
            .and(DISTRIBUTION_SALESPERSON.STATUS.eq(distDistributionSalespersonVO.getStatus()));

        return super.mapper.query(distDistributionSalespersonVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param distDistributionSalespersonVO
     */
    @Override
    public void update(DistributionSalespersonVO distDistributionSalespersonVO) {
        UpdateChain updateChain =
            this.updateChain().where(DISTRIBUTION_SALESPERSON.ID.eq(distDistributionSalespersonVO.getId()));

        updateChain.set(DISTRIBUTION_SALESPERSON.ID, distDistributionSalespersonVO.getId(), VerifyUtils::isNotEmpty);

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
