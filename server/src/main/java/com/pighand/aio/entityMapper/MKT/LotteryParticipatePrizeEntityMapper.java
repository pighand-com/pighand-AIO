package com.pighand.aio.entityMapper.MKT;

import com.pighand.aio.domain.MKT.LotteryParticipatePrizeDomain;
import com.pighand.aio.vo.MKT.LotteryParticipatePrizeVO;
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

    LotteryParticipatePrizeVO toVo(LotteryParticipatePrizeDomain entity);

    LotteryParticipatePrizeDomain toDomain(LotteryParticipatePrizeVO vo);

    List<LotteryParticipatePrizeVO> toVoList(List<LotteryParticipatePrizeDomain> entity);

    List<LotteryParticipatePrizeDomain> toDomainList(List<LotteryParticipatePrizeVO> vo);
}
