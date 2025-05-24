package com.pighand.aio.entityMapper.distribution;

import com.pighand.aio.domain.distribution.DistributionSalesSettlementDomain;
import com.pighand.aio.vo.distribution.DistributionSalesSettlementVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 分销 - 结算记录
 *
 * @author wangshuli
 * @createDate 2025-05-24 14:35:05
 */
@Mapper(componentModel = "spring")
public interface DistributionSalesSettlementEntityMapper {

    DistributionSalesSettlementVO toVo(DistributionSalesSettlementDomain entity);

    DistributionSalesSettlementDomain toDomain(DistributionSalesSettlementVO vo);

    List<DistributionSalesSettlementVO> toVoList(List<DistributionSalesSettlementDomain> entity);

    List<DistributionSalesSettlementDomain> toDomainList(List<DistributionSalesSettlementVO> vo);
}
