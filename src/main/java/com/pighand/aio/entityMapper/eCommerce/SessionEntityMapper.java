package com.pighand.aio.entityMapper.ECommerce;

import com.pighand.aio.domain.ECommerce.SessionDomain;
import com.pighand.aio.vo.ECommerce.SessionVO;
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
