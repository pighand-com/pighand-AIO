package com.pighand.aio.entityMapper.distribution;

import com.pighand.aio.domain.distribution.DistributionSalespersonDomain;
import com.pighand.aio.vo.distribution.DistributionSalespersonVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 分销 - 销售资格
 *
 * @author wangshuli
 * @createDate 2025-05-24 14:35:05
 */
@Mapper(componentModel = "spring")
public interface DistributionSalespersonEntityMapper {

    DistributionSalespersonVO toVo(DistributionSalespersonDomain entity);

    DistributionSalespersonDomain toDomain(DistributionSalespersonVO vo);

    List<DistributionSalespersonVO> toVoList(List<DistributionSalespersonDomain> entity);

    List<DistributionSalespersonDomain> toDomainList(List<DistributionSalespersonVO> vo);
}
