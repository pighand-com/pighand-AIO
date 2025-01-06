package com.pighand.aio.service.base.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.pighand.aio.domain.base.TenantDomain;
import com.pighand.aio.mapper.base.TenantMapper;
import com.pighand.aio.service.base.TenantService;
import com.pighand.aio.vo.base.TenantVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.base.table.TenantTableDef.TENANT;

/**
 * 租户
 *
 * @author wangshuli
 * @createDate 2024-12-31 19:04:50
 */
@Service
public class TenantServiceImpl extends BaseServiceImpl<TenantMapper, TenantDomain> implements TenantService {

    /**
     * 创建
     *
     * @param baseTenantVO
     * @return
     */
    @Override
    public TenantVO create(TenantVO baseTenantVO) {
        super.mapper.insert(baseTenantVO);

        return baseTenantVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public TenantDomain find(Long id) {
        return super.mapper.find(id, null);
    }

    /**
     * 分页或列表
     *
     * @param baseTenantVO
     * @return PageOrList<BaseTenantVO>
     */
    @Override
    public PageOrList<TenantVO> query(TenantVO baseTenantVO) {

        QueryWrapper queryWrapper = QueryWrapper.create()

            // like
            .and(TENANT.NAME.like(baseTenantVO.getName()))

            // equal
            .and(TENANT.APPLICATION_ID.eq(baseTenantVO.getApplicationId()));

        return super.mapper.query(baseTenantVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param baseTenantVO
     */
    @Override
    public void update(TenantVO baseTenantVO) {
        UpdateChain updateChain = this.updateChain().where(TENANT.ID.eq(baseTenantVO.getId()));

        updateChain.set(TENANT.ID, baseTenantVO.getId(), VerifyUtils::isNotEmpty);

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
