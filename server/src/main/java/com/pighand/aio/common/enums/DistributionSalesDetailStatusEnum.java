package com.pighand.aio.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import com.pighand.framework.spring.base.BaseEnum;
import lombok.Getter;

/**
 * 分销 - 销售明细 - 状态
 */
@Getter
public enum DistributionSalesDetailStatusEnum implements BaseEnum {
    /**
     * 冻结中
     */
    FROZEN(0),

    /**
     * 待结算
     */
    PENDING_SETTLEMENT(10),

    /**
     * 已结算
     */
    SETTLED(20),

    /**
     * 订单退款
     */
    REFUNDED(90);

    @JsonValue
    @EnumValue
    private final Integer value;

    DistributionSalesDetailStatusEnum(int value) {
        this.value = value;
    }
}
