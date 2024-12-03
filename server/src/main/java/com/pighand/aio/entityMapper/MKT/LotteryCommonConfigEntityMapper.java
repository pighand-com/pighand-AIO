package com.pighand.aio.entityMapper.MKT;

import com.pighand.user.domain.MktLotteryCommonConfigDomain;
import com.pighand.user.domain.MktLotteryCommonConfigVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 营销 - 抽奖配置
 *
 * @author wangshuli
 * @createDate 2024-11-28 17:21:35
 */
@Mapper(componentModel = "spring")
public interface LotteryCommonConfigEntityMapper {

    MktLotteryCommonConfigVO toVo(MktLotteryCommonConfigDomain entity);

    MktLotteryCommonConfigDomain toDomain(MktLotteryCommonConfigVO vo);

    List<MktLotteryCommonConfigVO> toVoList(List<MktLotteryCommonConfigDomain> entity);

    List<MktLotteryCommonConfigDomain> toDomainList(List<MktLotteryCommonConfigVO> vo);
}
