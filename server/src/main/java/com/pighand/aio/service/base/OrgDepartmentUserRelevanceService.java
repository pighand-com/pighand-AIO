package com.pighand.aio.service.base;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.base.OrgDepartmentUserRelevanceDomain;
import com.pighand.aio.mapper.base.OrgDepartmentUserRelevanceMapper;
import com.pighand.aio.service.BaseOrgDepartmentUserRelevanceService;
import com.pighand.aio.vo.base.OrgDepartmentUserRelevanceVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.stereotype.Service;

/**
 * 组织 - 部门-用户关系表
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@Service
public class OrgDepartmentUserRelevanceService extends BaseServiceImpl<OrgDepartmentUserRelevanceMapper, OrgDepartmentUserRelevanceDomain>
     implements BaseOrgDepartmentUserRelevanceService{

    /**
     * 创建
     *
     * @param baseOrgDepartmentUserRelevanceVO
     * @return
     */
    @Override
    public OrgDepartmentUserRelevanceVO create(OrgDepartmentUserRelevanceVO baseOrgDepartmentUserRelevanceVO) {
        super.mapper.insert(baseOrgDepartmentUserRelevanceVO);

        return baseOrgDepartmentUserRelevanceVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public OrgDepartmentUserRelevanceDomain find(Long id) {
        return super.mapper.find(id, null);
    }

    /**
     * 分页或列表
     *
     * @param baseOrgDepartmentUserRelevanceVO
     * @return PageOrList<BaseOrgDepartmentUserRelevanceVO>
     */
    @Override
    public PageOrList<OrgDepartmentUserRelevanceVO> query(OrgDepartmentUserRelevanceVO baseOrgDepartmentUserRelevanceVO) {

        QueryWrapper queryWrapper = QueryWrapper.create()


        // equal
        .and(BASE_ORG_DEPARTMENT_USER_RELEVANCE.DEPARTMENT_ID.eq(baseOrgDepartmentUserRelevanceVO.getDepartmentId()))
        .and(BASE_ORG_DEPARTMENT_USER_RELEVANCE.USER_ID.eq(baseOrgDepartmentUserRelevanceVO.getUserId()))
        ;

        return super.mapper.query(baseOrgDepartmentUserRelevanceVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param baseOrgDepartmentUserRelevanceVO
     */
    @Override
    public void update(OrgDepartmentUserRelevanceVO baseOrgDepartmentUserRelevanceVO) {
        UpdateChain updateChain = this.updateChain().where(BASE_ORG_DEPARTMENT_USER_RELEVANCE.ID.eq(baseOrgDepartmentUserRelevanceVO.getId()));

            updateChain.set(BASE_ORG_DEPARTMENT_USER_RELEVANCE.ID, baseOrgDepartmentUserRelevanceVO.getId(), , VerifyUtils::isNotEmpty);

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
