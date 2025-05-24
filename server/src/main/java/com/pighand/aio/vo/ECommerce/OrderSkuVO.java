package com.pighand.aio.vo.ECommerce;

import com.pighand.aio.domain.ECommerce.OrderSkuDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 电商 - 订单 - SKU
 *
 * @author wangshuli
 * @createDate 2024-04-18 14:35:34
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderSkuVO extends OrderSkuDomain {

    // relation table: begin
    private OrderVO order;

    private OrderTradeVO orderTrade;

    private List<BillVO> bill;
    // relation table: end

    // 是否缺货
    private boolean stockOut;
}
