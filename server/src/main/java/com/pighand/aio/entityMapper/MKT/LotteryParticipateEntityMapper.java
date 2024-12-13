package com.pighand.aio.entityMapper.MKT;

import com.pighand.aio.domain.MKT.LotteryParticipateDomain;
import com.pighand.aio.vo.MKT.LotteryParticipateVO;
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

    LotteryParticipateVO toVo(LotteryParticipateDomain entity);

    LotteryParticipateDomain toDomain(LotteryParticipateVO vo);

    List<LotteryParticipateVO> toVoList(List<LotteryParticipateDomain> entity);

    List<LotteryParticipateDomain> toDomainList(List<LotteryParticipateVO> vo);
}
