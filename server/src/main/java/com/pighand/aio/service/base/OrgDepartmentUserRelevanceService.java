package com.pighand.aio.service.base;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.pighand.aio.domain.base.OrgDepartmentUserRelevanceDomain;
import com.pighand.aio.mapper.base.OrgDepartmentUserRelevanceMapper;
import com.pighand.aio.vo.base.OrgDepartmentUserRelevanceVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.base.table.OrgDepartmentUserRelevanceTableDef.ORG_DEPARTMENT_USER_RELEVANCE;

/**
 * 组织 - 部门-用户关系表
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@Service
public class OrgDepartmentUserRelevanceService
    extends BaseServiceImpl<OrgDepartmentUserRelevanceMapper, OrgDepartmentUserRelevanceDomain> {

    /**
     * 创建
     *
     * @param baseOrgDepartmentUserRelevanceVO
     * @return
     */
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
    public OrgDepartmentUserRelevanceDomain find(Long id) {
        return super.mapper.find(id, null);
    }

    /**
     * 分页或列表
     *
     * @param baseOrgDepartmentUserRelevanceVO
     * @return PageOrList<BaseOrgDepartmentUserRelevanceVO>
     */
    public PageOrList<OrgDepartmentUserRelevanceVO> query(
        OrgDepartmentUserRelevanceVO baseOrgDepartmentUserRelevanceVO) {

        QueryWrapper queryWrapper = QueryWrapper.create()

            // equal
            .and(ORG_DEPARTMENT_USER_RELEVANCE.DEPARTMENT_ID.eq(baseOrgDepartmentUserRelevanceVO.getDepartmentId()))
            .and(ORG_DEPARTMENT_USER_RELEVANCE.USER_ID.eq(baseOrgDepartmentUserRelevanceVO.getUserId()));

        return super.mapper.query(baseOrgDepartmentUserRelevanceVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param baseOrgDepartmentUserRelevanceVO
     */
    public void update(OrgDepartmentUserRelevanceVO baseOrgDepartmentUserRelevanceVO) {
        UpdateChain updateChain =
            this.updateChain().where(ORG_DEPARTMENT_USER_RELEVANCE.ID.eq(baseOrgDepartmentUserRelevanceVO.getId()));

        updateChain.set(ORG_DEPARTMENT_USER_RELEVANCE.ID, baseOrgDepartmentUserRelevanceVO.getId(),
            VerifyUtils::isNotEmpty);

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
