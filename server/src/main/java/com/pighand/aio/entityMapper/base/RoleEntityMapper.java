package com.pighand.aio.entityMapper.base;

import com.pighand.aio.domain.base.RoleDomain;
import com.pighand.aio.vo.base.RoleVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 角色
 *
 * @author wangshuli
 * @createDate 2025-06-04 10:08:01
 */
@Mapper(componentModel = "spring")
public interface RoleEntityMapper {

    RoleVO toVo(RoleDomain entity);

    RoleDomain toDomain(RoleVO vo);

    List<RoleVO> toVoList(List<RoleDomain> entity);

    List<RoleDomain> toDomainList(List<RoleVO> vo);
}
