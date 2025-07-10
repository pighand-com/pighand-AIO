package com.pighand.aio.mapper.distribution;

import com.pighand.aio.domain.distribution.DistributionSalesDetailDomain;
import com.pighand.framework.spring.base.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 分销 - 销售记录
 *
 * @author wangshuli
 * @createDate 2025-05-24 14:35:05
 */
@Mapper
public interface DistributionSalesDetailMapper extends BaseMapper<DistributionSalesDetailDomain> {

}
