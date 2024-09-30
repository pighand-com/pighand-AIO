package com.pighand.aio.entityMapper.base;

import com.pighand.aio.domain.base.UserMessageDomain;
import com.pighand.aio.vo.base.UserMessageVO;
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
