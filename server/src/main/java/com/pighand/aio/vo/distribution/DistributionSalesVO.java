package com.pighand.aio.vo.distribution;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.ECommerce.OrderDomain;
import com.pighand.aio.domain.ECommerce.OrderSkuDomain;
import com.pighand.aio.domain.distribution.DistributionSalesDomain;
import com.pighand.aio.vo.ECommerce.TicketVO;
import com.pighand.framework.spring.api.annotation.serialization.ToListLongSerializer;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 分销 - 销售记录
 *
 * @author wangshuli
 * @createDate 2025-05-24 14:35:05
 */
@Data
@TableRef(DistributionSalesDomain.class)
@EqualsAndHashCode(callSuper = false)
public class DistributionSalesVO extends DistributionSalesDomain {

    // relation table: begin
    private OrderDomain order;

    private List<OrderSkuDomain> orderSku;

    private List<TicketVO> tickets;
    // relation table: end

    private Long statusCount;

    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @JsonDeserialize(using = ToListLongSerializer.class)
    private List<Long> settledDetailIds;
}
