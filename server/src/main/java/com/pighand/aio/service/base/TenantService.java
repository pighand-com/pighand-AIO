package com.pighand.aio.service.base;

import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.aio.domain.base.TenantDomain;
import com.pighand.aio.vo.base.TenantVO;

/**
 * 租户
 *
 * @author wangshuli
 * @createDate 2024-12-31 19:04:50
 */
public interface TenantService extends BaseService<TenantDomain> {

    /**
     * 创建
     *
     * @param baseTenantVO
     * @return
     */
    TenantVO create(TenantVO baseTenantVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    TenantDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param baseTenantVO
     * @return PageOrList<BaseTenantVO>
     */
    PageOrList<TenantVO> query(TenantVO baseTenantVO);

    /**
     * 修改
     *
     * @param baseTenantVO
     */
    void update(TenantVO baseTenantVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}
