package com.pighand.aio.entityMapper.MKT;

import com.pighand.user.domain.MktLotteryParticipateDomain;
import com.pighand.user.domain.MktLotteryParticipateVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 营销 - 抽奖 - 参与类型
 *
 * @author wangshuli
 * @createDate 2024-11-28 17:21:35
 */
@Mapper(componentModel = "spring")
public interface LotteryParticipateEntityMapper {

    MktLotteryParticipateVO toVo(MktLotteryParticipateDomain entity);

    MktLotteryParticipateDomain toDomain(MktLotteryParticipateVO vo);

    List<MktLotteryParticipateVO> toVoList(List<MktLotteryParticipateDomain> entity);

    List<MktLotteryParticipateDomain> toDomainList(List<MktLotteryParticipateVO> vo);
}
