package com.pighand.aio.vo.ECommerce;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.ECommerce.OrderTradeDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 电商 - 订单 - 交易单
 *
 * @author wangshuli
 * @createDate 2024-04-18 14:35:34
 */
@Data
@TableRef(OrderTradeDomain.class)
@EqualsAndHashCode(callSuper = false)
public class OrderTradeVO extends OrderTradeDomain {

    // relation table: begin
    private List<OrderVO> order;

    private List<OrderSkuVO> orderSku;

    private List<BillVO> bill;
    // relation table: end
}
