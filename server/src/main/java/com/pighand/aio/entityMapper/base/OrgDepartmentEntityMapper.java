package com.pighand.aio.entityMapper.base;

import com.pighand.aio.domain.base.OrgDepartmentDomain;
import com.pighand.aio.vo.base.OrgDepartmentVO;
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

    OrgDepartmentVO toVo(OrgDepartmentDomain entity);

    OrgDepartmentDomain toDomain(OrgDepartmentVO vo);

    List<OrgDepartmentVO> toVoList(List<OrgDepartmentDomain> entity);

    List<OrgDepartmentDomain> toDomainList(List<OrgDepartmentVO> vo);
}
