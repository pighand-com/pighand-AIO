package com.pighand.aio.entityMapper.base;

import com.pighand.aio.domain.base.TenantDomain;
import com.pighand.aio.vo.base.TenantVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 租户
 *
 * @author wangshuli
 * @createDate 2024-12-31 19:04:50
 */
@Mapper(componentModel = "spring")
public interface TenantEntityMapper {

    TenantVO toVo(TenantDomain entity);

    TenantDomain toDomain(TenantVO vo);

    List<TenantVO> toVoList(List<TenantDomain> entity);

    List<TenantDomain> toDomainList(List<TenantVO> vo);
}
