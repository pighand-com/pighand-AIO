package com.pighand.aio.entityMapper.user;

import com.pighand.aio.domain.user.UserDouyinDomain;
import com.pighand.aio.vo.user.UserDouyinVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 用户 - 抖音
 *
 * @author wangshuli
 * @createDate 2024-06-05 23:58:27
 */
@Mapper(componentModel = "spring")
public interface UserDouyinEntityMapper {

    UserDouyinVO toVo(UserDouyinDomain entity);

    UserDouyinDomain toDomain(UserDouyinVO vo);

    List<UserDouyinVO> toVoList(List<UserDouyinDomain> entity);

    List<UserDouyinDomain> toDomainList(List<UserDouyinVO> vo);
}
