package com.pighand.aio.entityMapper.distribution;

import com.pighand.aio.domain.distribution.DistributionGoodsRuleDomain;
import com.pighand.aio.vo.distribution.DistributionGoodsRuleVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 分销 - 分销商品规则
 *
 * @author wangshuli
 * @createDate 2025-05-24 14:35:05
 */
@Mapper(componentModel = "spring")
public interface DistributionGoodsRuleEntityMapper {

    DistributionGoodsRuleVO toVo(DistributionGoodsRuleDomain entity);

    DistributionGoodsRuleDomain toDomain(DistributionGoodsRuleVO vo);

    List<DistributionGoodsRuleVO> toVoList(List<DistributionGoodsRuleDomain> entity);

    List<DistributionGoodsRuleDomain> toDomainList(List<DistributionGoodsRuleVO> vo);
}
