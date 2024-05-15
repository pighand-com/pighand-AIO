package com.pighand.aio.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;

/**
 * 账单类型
 */
public enum BillTypeEnum {
    /**
     * 支付
     */
    PAYMENT(1),

    /**
     * 退款
     */
    REFUND(2);

    @JsonValue
    @EnumValue
    public final Integer value;

    BillTypeEnum(Integer value) {
        this.value = value;
    }
}
