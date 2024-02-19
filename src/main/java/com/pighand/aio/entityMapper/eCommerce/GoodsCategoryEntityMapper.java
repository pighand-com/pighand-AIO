package com.pighand.aio.entityMapper.eCommerce;

import com.pighand.aio.domain.eCommerce.GoodsCategoryDomain;
import com.pighand.aio.vo.eCommerce.GoodsCategoryVO;
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
