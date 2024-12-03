package com.pighand.aio.entityMapper.MKT;

import com.pighand.user.domain.MktOtteryParticipatePrizeDomain;
import com.pighand.user.domain.MktOtteryParticipatePrizeVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 营销 - 抽奖 - 参与类型 - 奖品
 *
 * @author wangshuli
 * @createDate 2024-11-28 17:21:35
 */
@Mapper(componentModel = "spring")
public interface LotteryParticipatePrizeEntityMapper {

    MktOtteryParticipatePrizeVO toVo(MktOtteryParticipatePrizeDomain entity);

    MktOtteryParticipatePrizeDomain toDomain(MktOtteryParticipatePrizeVO vo);

    List<MktOtteryParticipatePrizeVO> toVoList(List<MktOtteryParticipatePrizeDomain> entity);

    List<MktOtteryParticipatePrizeDomain> toDomainList(List<MktOtteryParticipatePrizeVO> vo);
}
