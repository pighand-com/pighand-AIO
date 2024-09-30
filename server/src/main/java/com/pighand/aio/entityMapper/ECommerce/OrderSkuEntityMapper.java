package com.pighand.aio.entityMapper.ECommerce;

import com.pighand.aio.domain.ECommerce.OrderSkuDomain;
import com.pighand.aio.vo.ECommerce.OrderSkuVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 电商 - 订单 - SKU
 *
 * @author wangshuli
 * @createDate 2024-04-18 14:35:34
 */
@Mapper(componentModel = "spring")
public interface OrderSkuEntityMapper {

    OrderSkuVO toVo(OrderSkuDomain entity);

    OrderSkuDomain toDomain(OrderSkuVO vo);

    List<OrderSkuVO> toVoList(List<OrderSkuDomain> entity);

    List<OrderSkuDomain> toDomainList(List<OrderSkuVO> vo);
}
