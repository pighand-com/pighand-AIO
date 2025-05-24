package com.pighand.aio.vo.ECommerce;

import com.pighand.aio.domain.ECommerce.BillDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商城 - 账单
 *
 * @author wangshuli
 * @createDate 2024-04-18 14:35:34
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BillVO extends BillDomain {

    // relation table: begin
    private OrderVO order;

    private OrderTradeVO orderTrade;

    private OrderSkuVO orderSku;
    // relation table: end
}
