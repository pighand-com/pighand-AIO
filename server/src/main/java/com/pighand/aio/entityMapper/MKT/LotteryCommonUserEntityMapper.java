package com.pighand.aio.entityMapper.MKT;

import com.pighand.aio.domain.MKT.LotteryCommonUserDomain;
import com.pighand.aio.vo.MKT.LotteryCommonUserVO;
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

    LotteryCommonUserVO toVo(LotteryCommonUserDomain entity);

    LotteryCommonUserDomain toDomain(LotteryCommonUserVO vo);

    List<LotteryCommonUserVO> toVoList(List<LotteryCommonUserDomain> entity);

    List<LotteryCommonUserDomain> toDomainList(List<LotteryCommonUserVO> vo);
}
