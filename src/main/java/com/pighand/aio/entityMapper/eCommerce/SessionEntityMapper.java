package com.pighand.aio.entityMapper.eCommerce;

import com.pighand.aio.domain.eCommerce.SessionDomain;
import com.pighand.aio.vo.eCommerce.SessionVO;
import org.mapstruct.Mapper;

/**
 * 电商 - 场次
 *
 * @author wangshuli
 * @createDate 2023-12-05 16:13:27
 */
@Mapper(componentModel = "spring")
public interface SessionEntityMapper {

    SessionVO toVo(SessionDomain entity);

    SessionDomain toDomain(SessionVO vo);
}
