package com.pighand.aio.service.base;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.base.OrgDepartmentDomain;
import com.pighand.aio.mapper.base.OrgDepartmentMapper;
import com.pighand.aio.service.BaseOrgDepartmentService;
import com.pighand.aio.vo.base.OrgDepartmentVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.stereotype.Service;

/**
 * 组织 - 部门
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@Service
public class OrgDepartmentService extends BaseServiceImpl<OrgDepartmentMapper, OrgDepartmentDomain>
     implements BaseOrgDepartmentService{

    /**
     * 创建
     *
     * @param baseOrgDepartmentVO
     * @return
     */
    @Override
    public OrgDepartmentVO create(OrgDepartmentVO baseOrgDepartmentVO) {
        super.mapper.insert(baseOrgDepartmentVO);

        return baseOrgDepartmentVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public OrgDepartmentDomain find(Long id) {
        return super.mapper.find(id, null);
    }

    /**
     * 分页或列表
     *
     * @param baseOrgDepartmentVO
     * @return PageOrList<BaseOrgDepartmentVO>
     */
    @Override
    public PageOrList<OrgDepartmentVO> query(OrgDepartmentVO baseOrgDepartmentVO) {

        QueryWrapper queryWrapper = QueryWrapper.create()

        // like
        .and(BASE_ORG_DEPARTMENT.NAME.like(baseOrgDepartmentVO.getName()))

        ;

        return super.mapper.query(baseOrgDepartmentVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param baseOrgDepartmentVO
     */
    @Override
    public void update(OrgDepartmentVO baseOrgDepartmentVO) {
        UpdateChain updateChain = this.updateChain().where(BASE_ORG_DEPARTMENT.ID.eq(baseOrgDepartmentVO.getId()));

            updateChain.set(BASE_ORG_DEPARTMENT.ID, baseOrgDepartmentVO.getId(), , VerifyUtils::isNotEmpty);

        if (updateTmpColumns.size() > 0) {
            updateChain.update();
        }
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
