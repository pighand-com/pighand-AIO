package com.pighand.aio.vo.ECommerce;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.ECommerce.OrderDomain;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 电商 - 订单
 *
 * @author wangshuli
 * @createDate 2024-04-18 14:35:34
 */
@Data
@TableRef(OrderDomain.class)
@EqualsAndHashCode(callSuper = false)
public class OrderVO extends OrderDomain {

    // relation table: begin
    private List<GoodsSpuVO> goodsSpu;

    private List<TicketVO> ticket;

    private List<OrderSkuVO> orderSku;

    private OrderTradeVO orderTrade;

    private List<BillVO> bill;
    // relation table: end

    // 支付平台
    private String outTradePlatform;

    // 答题
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long deviceId;

    private String message;

    // 分销ID
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long salespersonId;
}
