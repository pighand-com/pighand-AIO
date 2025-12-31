package com.pighand.aio.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import com.pighand.framework.spring.base.BaseEnum;
import lombok.Getter;

/**
 * 电商 - 订单 - 交易状态
 */
@Getter
public enum OrderTradeStatusEnum implements BaseEnum {
    /**
     * 待支付
     */
    PENDING_PAYMENT(10),

    /**
     * 已支付/待发货/部分发货
     */
    PAID(20),

    /**
     * 全部已发货/待收货
     */
    SHIPPED(30),

    /**
     * 确认收货
     */
    RECEIVED(40),

    /**
     * 超时取消
     */
    CANCELLED(50),

    /**
     * 全部退款
     */
    REFUNDED(51);

    @JsonValue
    @EnumValue
    private final Integer value;

    OrderTradeStatusEnum(int value) {
        this.value = value;
    }
}
