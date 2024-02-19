package com.pighand.aio.entityMapper.eCommerce;

import com.pighand.aio.domain.eCommerce.SessionUserGroupDomain;
import com.pighand.aio.vo.eCommerce.SessionUserGroupVO;
import org.mapstruct.Mapper;

/**
 * 电商 - 场次 - 用户分组
 *
 * @author wangshuli
 * @createDate 2023-12-05 16:13:27
 */
@Mapper(componentModel = "spring")
public interface SessionUserGroupEntityMapper {

    SessionUserGroupVO toVo(SessionUserGroupDomain entity);

    SessionUserGroupDomain toDomain(SessionUserGroupVO vo);
}
