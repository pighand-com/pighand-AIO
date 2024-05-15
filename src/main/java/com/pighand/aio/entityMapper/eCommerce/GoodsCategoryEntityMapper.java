package com.pighand.aio.entityMapper.ECommerce;

import com.pighand.aio.domain.ECommerce.GoodsCategoryDomain;
import com.pighand.aio.vo.ECommerce.GoodsCategoryVO;
import org.mapstruct.Mapper;

/**
 * 电商 - 商品类目
 *
 * @author wangshuli
 * @createDate 2024-01-08 11:05:18
 */
@Mapper(componentModel = "spring")
public interface GoodsCategoryEntityMapper {

    GoodsCategoryVO toVo(GoodsCategoryDomain entity);

    GoodsCategoryDomain toDomain(GoodsCategoryVO vo);
}
