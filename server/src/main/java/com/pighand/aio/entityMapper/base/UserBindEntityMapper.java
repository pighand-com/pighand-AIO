package com.pighand.aio.entityMapper.base;

import com.pighand.aio.domain.base.UserBindDomain;
import com.pighand.aio.vo.base.UserBindVO;
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
