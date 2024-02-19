package com.pighand.aio.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;

/**
 * 优惠券 - 已领 - 状态
 */
public enum CouponUserStatusEnum {
    /**
     * 未使用
     */
    UNUSED(10),

    /**
     * 商户核销
     */
    VERIFY(20);

    @JsonValue
    @EnumValue
    public final Integer value;

    CouponUserStatusEnum(int value) {
        this.value = value;
    }
}
