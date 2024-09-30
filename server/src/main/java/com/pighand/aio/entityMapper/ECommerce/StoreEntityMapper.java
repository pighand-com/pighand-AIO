package com.pighand.aio.entityMapper.ECommerce;

import com.pighand.aio.domain.ECommerce.StoreDomain;
import com.pighand.aio.vo.ECommerce.StoreVO;
import org.mapstruct.Mapper;

/**
 * 电商 - 门店
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Mapper(componentModel = "spring")
public interface StoreEntityMapper {

    StoreVO toVo(StoreDomain entity);

    StoreDomain toDomain(StoreVO vo);
}
