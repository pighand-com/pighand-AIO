package com.pighand.aio.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import com.pighand.framework.spring.base.BaseEnum;
import lombok.Getter;

/**
 * 电商 - 订单 - 退款状态
 */
@Getter
public enum OrderRefundStatusEnum implements BaseEnum {
    /**
     * 不可退款
     */
    NOT_REFUNDABLE(10),

    /**
     * 可退款
     */
    REFUNDABLE(11),

    /**
     * 退款申请
     */
    APPLIED(20),

    /**
     * 退款中
     */
    REFUNDING(30),

    /**
     * 已退款
     */
    REFUNDED(40);

    @JsonValue
    @EnumValue
    private final Integer value;

    OrderRefundStatusEnum(int value) {
        this.value = value;
    }
}
