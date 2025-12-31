package com.pighand.aio.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import com.pighand.framework.spring.base.BaseEnum;
import lombok.Getter;

/**
 * 电商 - 订单 - 评价状态
 */
@Getter
public enum OrderEvaluationStatusEnum implements BaseEnum {
    /**
     * 不可评价
     */
    NOT_EVALUATABLE(10),

    /**
     * 待评价
     */
    PENDING(20),

    /**
     * 已评价
     */
    EVALUATED(30);

    @JsonValue
    @EnumValue
    private final Integer value;

    OrderEvaluationStatusEnum(int value) {
        this.value = value;
    }
}
