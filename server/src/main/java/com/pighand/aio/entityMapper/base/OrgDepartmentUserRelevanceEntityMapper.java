package com.pighand.aio.entityMapper.base;

import com.pighand.aio.domain.base.OrgDepartmentUserRelevanceDomain;
import com.pighand.aio.vo.base.OrgDepartmentUserRelevanceVO;
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

    OrgDepartmentUserRelevanceVO toVo(OrgDepartmentUserRelevanceDomain entity);

    OrgDepartmentUserRelevanceDomain toDomain(OrgDepartmentUserRelevanceVO vo);

    List<OrgDepartmentUserRelevanceVO> toVoList(List<OrgDepartmentUserRelevanceDomain> entity);

    List<OrgDepartmentUserRelevanceDomain> toDomainList(List<OrgDepartmentUserRelevanceVO> vo);
}
