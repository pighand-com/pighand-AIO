package com.pighand.aio.entityMapper.ECommerce;

import com.pighand.aio.domain.ECommerce.SessionUserCycleDomain;
import com.pighand.aio.vo.ECommerce.SessionUserCycleVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 电商 - 场次 - 用户周期
 *
 * @author wangshuli
 * @createDate 2024-05-23 15:01:58
 */
@Mapper(componentModel = "spring")
public interface SessionUserCycleEntityMapper {

    SessionUserCycleVO toVo(SessionUserCycleDomain entity);

    SessionUserCycleDomain toDomain(SessionUserCycleVO vo);

    List<SessionUserCycleVO> toVoList(List<SessionUserCycleDomain> entity);

    List<SessionUserCycleDomain> toDomainList(List<SessionUserCycleVO> vo);
}
