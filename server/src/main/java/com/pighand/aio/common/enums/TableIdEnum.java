package com.pighand.aio.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;

/**
 * 业务类型枚举，用于生成订单前缀
 */
public enum TableIdEnum {
    /**
     * 交易单
     */
    ORDER_TRADE(1),

    /**
     * 订单
     */
    ORDER(2),

    /**
     * 账单
     */
    BILL(3);

    @JsonValue
    @EnumValue
    public final Integer value;

    TableIdEnum(Integer value) {
        this.value = value;
    }
}
