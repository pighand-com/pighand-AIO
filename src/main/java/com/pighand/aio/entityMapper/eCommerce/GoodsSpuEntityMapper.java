package com.pighand.aio.entityMapper.eCommerce;

import com.pighand.aio.domain.eCommerce.GoodsSpuDomain;
import com.pighand.aio.vo.eCommerce.GoodsSpuVO;
import org.mapstruct.Mapper;

/**
 * 电商 - SPU
 *
 * @author wangshuli
 * @createDate 2024-01-07 19:55:48
 */
@Mapper(componentModel = "spring")
public interface GoodsSpuEntityMapper {

    GoodsSpuVO toVo(GoodsSpuDomain entity);

    GoodsSpuDomain toDomain(GoodsSpuVO vo);
}
