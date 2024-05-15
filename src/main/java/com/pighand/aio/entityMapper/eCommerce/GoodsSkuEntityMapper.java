package com.pighand.aio.entityMapper.ECommerce;

import com.pighand.aio.domain.ECommerce.GoodsSkuDomain;
import com.pighand.aio.vo.ECommerce.GoodsSkuVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 电商 - SKU
 *
 * @author wangshuli
 * @createDate 2024-01-07 19:55:48
 */
@Mapper(componentModel = "spring")
public interface GoodsSkuEntityMapper {

    GoodsSkuVO toVo(GoodsSkuDomain entity);

    GoodsSkuDomain toDomain(GoodsSkuVO vo);

    List<GoodsSkuDomain> toDomainList(List<GoodsSkuVO> voList);
}
