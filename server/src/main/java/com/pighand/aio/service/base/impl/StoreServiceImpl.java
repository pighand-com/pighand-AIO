package com.pighand.aio.service.base.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.pighand.aio.domain.base.StoreDomain;
import com.pighand.aio.mapper.base.StoreMapper;
import com.pighand.aio.service.base.StoreService;
import com.pighand.aio.vo.base.StoreVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.base.table.ApplicationTableDef.APPLICATION;
import static com.pighand.aio.domain.base.table.StoreTableDef.STORE;
import static com.pighand.aio.domain.base.table.TenantTableDef.TENANT;
import static com.pighand.aio.domain.base.table.UserExtensionTableDef.USER_EXTENSION;

/**
 * 门店
 *
 * @author wangshuli
 * @createDate 2024-12-31 19:04:50
 */
@Service
public class StoreServiceImpl extends BaseServiceImpl<StoreMapper, StoreDomain> implements StoreService {

    /**
     * 创建
     *
     * @param baseStoreVO
     * @return
     */
    @Override
    public StoreVO create(StoreVO baseStoreVO) {
        super.mapper.insert(baseStoreVO);

        return baseStoreVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public StoreDomain find(Long id) {
        return super.mapper.find(id);
    }

    /**
     * 分页或列表
     *
     * @param baseStoreVO
     * @return PageOrList<BaseStoreVO>
     */
    @Override
    public PageOrList<StoreVO> query(StoreVO baseStoreVO) {
        baseStoreVO.setJoinTables(APPLICATION.getName(), USER_EXTENSION.getName(), TENANT.getName());

        QueryWrapper queryWrapper = QueryWrapper.create().select(STORE.DEFAULT_COLUMNS)

            // like
            .and(STORE.NAME.like(baseStoreVO.getName()))

            // equal
            .and(STORE.APPLICATION_ID.eq(baseStoreVO.getApplicationId()))
            .and(STORE.TENANT_ID.eq(baseStoreVO.getTenantId()))

            // between
            .and(STORE.CREATED_AT.between(baseStoreVO.getCreatedAtRange()));

        return super.mapper.query(baseStoreVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param baseStoreVO
     */
    @Override
    public void update(StoreVO baseStoreVO) {
        UpdateChain updateChain = this.updateChain().where(STORE.ID.eq(baseStoreVO.getId()));

        updateChain.set(STORE.ID, baseStoreVO.getId(), VerifyUtils::isNotEmpty);

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
