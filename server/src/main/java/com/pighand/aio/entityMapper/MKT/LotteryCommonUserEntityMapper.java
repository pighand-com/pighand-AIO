package com.pighand.aio.entityMapper.MKT;

import com.pighand.user.domain.MktLotteryCommonUserDomain;
import com.pighand.user.domain.MktLotteryCommonUserVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 营销 - 抽奖参与用户
 *
 * @author wangshuli
 * @createDate 2024-11-28 17:21:35
 */
@Mapper(componentModel = "spring")
public interface LotteryCommonUserEntityMapper {

    MktLotteryCommonUserVO toVo(MktLotteryCommonUserDomain entity);

    MktLotteryCommonUserDomain toDomain(MktLotteryCommonUserVO vo);

    List<MktLotteryCommonUserVO> toVoList(List<MktLotteryCommonUserDomain> entity);

    List<MktLotteryCommonUserDomain> toDomainList(List<MktLotteryCommonUserVO> vo);
}
