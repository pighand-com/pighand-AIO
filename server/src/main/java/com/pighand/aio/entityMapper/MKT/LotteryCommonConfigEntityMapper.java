package com.pighand.aio.entityMapper.MKT;

import com.pighand.aio.domain.MKT.LotteryCommonConfigDomain;
import com.pighand.aio.vo.MKT.LotteryCommonConfigVO;
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

    LotteryCommonConfigVO toVo(LotteryCommonConfigDomain entity);

    LotteryCommonConfigDomain toDomain(LotteryCommonConfigVO vo);

    List<LotteryCommonConfigVO> toVoList(List<LotteryCommonConfigDomain> entity);

    List<LotteryCommonConfigDomain> toDomainList(List<LotteryCommonConfigVO> vo);
}
