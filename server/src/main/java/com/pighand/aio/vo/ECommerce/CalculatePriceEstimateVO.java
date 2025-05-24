package com.pighand.aio.vo.ECommerce;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * 价格试算
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CalculatePriceEstimateVO {
    private List<OrderVO> order;

    private BigDecimal amountPayable;

    private BigDecimal amountPaid;
}
