package com.pighand.aio.entityMapper.base;

import com.pighand.user.domain.BaseOrgDepartmentDomain;
import com.pighand.user.domain.BaseOrgDepartmentVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 组织 - 部门
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@Mapper(componentModel = "spring")
public interface OrgDepartmentEntityMapper {

    BaseOrgDepartmentVO toVo(BaseOrgDepartmentDomain entity);

    BaseOrgDepartmentDomain toDomain(BaseOrgDepartmentVO vo);

    List<BaseOrgDepartmentVO> toVoList(List<BaseOrgDepartmentDomain> entity);

    List<BaseOrgDepartmentDomain> toDomainList(List<BaseOrgDepartmentVO> vo);
}
