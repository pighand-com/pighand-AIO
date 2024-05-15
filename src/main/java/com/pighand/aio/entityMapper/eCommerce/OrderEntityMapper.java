package com.pighand.aio.entityMapper.ECommerce;

import com.pighand.aio.domain.ECommerce.OrderDomain;
import com.pighand.aio.vo.ECommerce.OrderVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 电商 - 订单
 *
 * @author wangshuli
 * @createDate 2024-04-18 14:35:34
 */
@Mapper(componentModel = "spring")
public interface OrderEntityMapper {

    OrderVO toVo(OrderDomain entity);

    OrderDomain toDomain(OrderVO vo);

    List<OrderVO> toVoList(List<OrderDomain> entity);

    List<OrderDomain> toDomainList(List<OrderVO> vo);
}
