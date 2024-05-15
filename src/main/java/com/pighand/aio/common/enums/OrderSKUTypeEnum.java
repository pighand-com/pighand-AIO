package com.pighand.aio.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;

/**
 * 订单SKU类型
 */
public enum OrderSKUTypeEnum {
    /**
     * SKU
     */
    SKU(10),

    /**
     * 票务
     */
    TICKET(20),

    /**
     * 场次
     */
    SESSION(30);

    @JsonValue
    @EnumValue
    public final Integer value;

    OrderSKUTypeEnum(Integer value) {
        this.value = value;
    }
}
