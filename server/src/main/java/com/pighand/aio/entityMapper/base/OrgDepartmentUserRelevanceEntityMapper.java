package com.pighand.aio.entityMapper.base;

import com.pighand.user.domain.BaseOrgDepartmentUserRelevanceDomain;
import com.pighand.user.domain.BaseOrgDepartmentUserRelevanceVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 组织 - 部门-用户关系表
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@Mapper(componentModel = "spring")
public interface OrgDepartmentUserRelevanceEntityMapper {

    BaseOrgDepartmentUserRelevanceVO toVo(BaseOrgDepartmentUserRelevanceDomain entity);

    BaseOrgDepartmentUserRelevanceDomain toDomain(BaseOrgDepartmentUserRelevanceVO vo);

    List<BaseOrgDepartmentUserRelevanceVO> toVoList(List<BaseOrgDepartmentUserRelevanceDomain> entity);

    List<BaseOrgDepartmentUserRelevanceDomain> toDomainList(List<BaseOrgDepartmentUserRelevanceVO> vo);
}
