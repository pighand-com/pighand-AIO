package com.pighand.aio.service.base;

import com.pighand.aio.domain.base.RoleDomain;
import com.pighand.aio.vo.base.RoleVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 角色
 *
 * @author wangshuli
 * @createDate 2025-06-04 10:08:01
 */
public interface RoleService extends BaseService<RoleDomain> {

    /**
     * 创建
     *
     * @param baseRoleVO
     * @return
     */
    RoleVO create(RoleVO baseRoleVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    RoleDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param baseRoleVO
     * @return PageOrList<BaseRoleVO>
     */
    PageOrList<RoleVO> query(RoleVO baseRoleVO);

    /**
     * 修改
     *
     * @param baseRoleVO
     */
    void update(RoleVO baseRoleVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}
