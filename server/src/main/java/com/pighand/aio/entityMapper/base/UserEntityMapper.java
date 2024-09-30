package com.pighand.aio.entityMapper.base;

import com.pighand.aio.domain.base.UserDomain;
import com.pighand.aio.vo.base.UserVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {
    UserVO toVo(UserDomain entity);

    UserDomain toDomain(UserVO vo);
}
