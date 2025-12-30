package com.pighand.aio.service.base;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.pighand.aio.domain.base.RoleDomain;
import com.pighand.aio.mapper.base.RoleMapper;
import com.pighand.aio.vo.base.RoleVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.stereotype.Service;

import static com.pighand.aio.common.dataPermission.ignore.RunWithIgnore.IgnoreDataPermission;
import static com.pighand.aio.domain.base.table.RoleTableDef.ROLE;

/**
 * 角色
 *
 * @author wangshuli
 * @createDate 2025-06-04 10:08:01
 */
@Service
public class RoleService extends BaseServiceImpl<RoleMapper, RoleDomain>  {

    /**
     * 创建
     *
     * @param baseRoleVO
     * @return
     */
    public RoleVO create(RoleVO baseRoleVO) {
        super.mapper.insert(baseRoleVO);

        return baseRoleVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public RoleDomain find(Long id) {
        return super.mapper.find(id);
    }

    /**
     * 分页或列表
     *
     * @param baseRoleVO
     * @return PageOrList<BaseRoleVO>
     */
    public PageOrList<RoleVO> query(RoleVO baseRoleVO) {

        QueryWrapper queryWrapper = QueryWrapper.create()

            // like
            .and(ROLE.NAME.like(baseRoleVO.getName()))

            // equal
            .and(ROLE.APPLICATION_ID.eq(baseRoleVO.getApplicationId()));

        // TODO 数据权限
        return IgnoreDataPermission(() -> mapper.query(baseRoleVO, queryWrapper));
    }

    /**
     * 修改
     *
     * @param baseRoleVO
     */
    public void update(RoleVO baseRoleVO) {
        UpdateChain updateChain = this.updateChain().where(ROLE.ID.eq(baseRoleVO.getId()));

        updateChain.set(ROLE.ID, baseRoleVO.getId(), VerifyUtils::isNotEmpty);

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
