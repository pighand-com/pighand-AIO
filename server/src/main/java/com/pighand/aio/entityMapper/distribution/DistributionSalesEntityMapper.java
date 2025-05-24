package com.pighand.aio.entityMapper.distribution;

import com.pighand.aio.domain.distribution.DistributionSalesDomain;
import com.pighand.aio.vo.distribution.DistributionSalesVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 分销 - 销售记录
 *
 * @author wangshuli
 * @createDate 2025-05-24 14:35:05
 */
@Mapper(componentModel = "spring")
public interface DistributionSalesEntityMapper {

    DistributionSalesVO toVo(DistributionSalesDomain entity);

    DistributionSalesDomain toDomain(DistributionSalesVO vo);

    List<DistributionSalesVO> toVoList(List<DistributionSalesDomain> entity);

    List<DistributionSalesDomain> toDomainList(List<DistributionSalesVO> vo);
}
