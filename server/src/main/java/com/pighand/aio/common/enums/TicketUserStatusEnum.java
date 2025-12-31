package com.pighand.aio.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import com.pighand.framework.spring.base.BaseEnum;
import lombok.Getter;

/**
 * 电商 - 票务 - 用户状态
 */
@Getter
public enum TicketUserStatusEnum implements BaseEnum {
    /**
     * 未核销
     */
    UNUSED(10),

    /**
     * 已核销
     */
    USED(20),

    /**
     * 已退款/失效
     */
    REFUNDED(99);

    @JsonValue
    @EnumValue
    private final Integer value;

    TicketUserStatusEnum(int value) {
        this.value = value;
    }
}
