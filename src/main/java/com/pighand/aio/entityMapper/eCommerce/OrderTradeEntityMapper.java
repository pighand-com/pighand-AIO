package com.pighand.aio.entityMapper.ECommerce;

import com.pighand.aio.domain.ECommerce.OrderTradeDomain;
import com.pighand.aio.vo.ECommerce.OrderTradeVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 电商 - 订单 - 交易单
 *
 * @author wangshuli
 * @createDate 2024-04-18 14:35:34
 */
@Mapper(componentModel = "spring")
public interface OrderTradeEntityMapper {

    OrderTradeVO toVo(OrderTradeDomain entity);

    OrderTradeDomain toDomain(OrderTradeVO vo);

    List<OrderTradeVO> toVoList(List<OrderTradeDomain> entity);

    List<OrderTradeDomain> toDomainList(List<OrderTradeVO> vo);
}
