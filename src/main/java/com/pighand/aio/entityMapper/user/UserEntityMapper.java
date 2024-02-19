package com.pighand.aio.entityMapper.user;

import com.pighand.aio.domain.user.UserDomain;
import com.pighand.aio.vo.user.UserVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {
    UserVO toVo(UserDomain entity);

    UserDomain toDomain(UserVO vo);
}
