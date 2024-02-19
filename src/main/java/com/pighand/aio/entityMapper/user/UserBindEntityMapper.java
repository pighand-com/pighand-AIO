package com.pighand.aio.entityMapper.user;

import com.pighand.aio.domain.user.UserBindDomain;
import com.pighand.aio.vo.user.UserBindVO;
import org.mapstruct.Mapper;

/**
 * 用户 - 绑定信息
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Mapper(componentModel = "spring")
public interface UserBindEntityMapper {

    UserBindVO toVo(UserBindDomain entity);

    UserBindDomain toDomain(UserBindVO vo);
}
