package com.pighand.aio.entityMapper.user;

import com.pighand.aio.domain.user.UserMessageDomain;
import com.pighand.aio.vo.user.UserMessageVO;
import org.mapstruct.Mapper;

/**
 * 用户 - 消息
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Mapper(componentModel = "spring")
public interface UserMessageEntityMapper {

    UserMessageVO toVo(UserMessageDomain entity);

    UserMessageDomain toDomain(UserMessageVO vo);
}
