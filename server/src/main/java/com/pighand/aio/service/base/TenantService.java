package com.pighand.aio.service.base;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.pighand.aio.domain.base.TenantDomain;
import com.pighand.aio.domain.base.table.UserTableDef;
import com.pighand.aio.mapper.base.TenantMapper;
import com.pighand.aio.vo.base.TenantVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.base.table.ApplicationTableDef.APPLICATION;
import static com.pighand.aio.domain.base.table.TenantTableDef.TENANT;
import static com.pighand.aio.domain.base.table.UserExtensionTableDef.USER_EXTENSION;
import static com.pighand.aio.domain.base.table.UserTableDef.USER;

/**
 * 租户
 *
 * @author wangshuli
 * @createDate 2024-12-31 19:04:50
 */
@Service
public class TenantService extends BaseServiceImpl<TenantMapper, TenantDomain>  {

    private static UserTableDef getUser() {
        return USER;
    }

    /**
     * 创建
     *
     * @param baseTenantVO
     * @return
     */
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
    public TenantDomain find(Long id) {
        return super.mapper.find(id);
    }

    /**
     * 分页或列表
     *
     * @param baseTenantVO
     * @return PageOrList<BaseTenantVO>
     */
    public PageOrList<TenantVO> query(TenantVO baseTenantVO) {
        baseTenantVO.setJoinTables(APPLICATION.getName(), USER_EXTENSION.getName());

        QueryWrapper queryWrapper = QueryWrapper.create().select(TENANT.DEFAULT_COLUMNS)

            // like
            .and(TENANT.NAME.like(baseTenantVO.getName()))

            // equal
            .and(TENANT.APPLICATION_ID.eq(baseTenantVO.getApplicationId()))

            // between
            .and(TENANT.CREATED_AT.between(baseTenantVO.getCreatedAtRange()));

        return super.mapper.query(baseTenantVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param baseTenantVO
     */
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
    public void delete(Long id) {
        super.mapper.deleteById(id);
    }
}
