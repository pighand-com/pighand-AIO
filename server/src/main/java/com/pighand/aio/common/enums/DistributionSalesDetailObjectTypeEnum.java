package com.pighand.aio.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import com.pighand.framework.spring.base.BaseEnum;
import lombok.Getter;

/**
 * 分销 - 销售明细 - 对象类型
 */
@Getter
public enum DistributionSalesDetailObjectTypeEnum implements BaseEnum {
    /**
     * 主题
     */
    THEME(10),

    /**
     * 票务
     */
    TICKET(20);

    @JsonValue
    @EnumValue
    private final Integer value;

    DistributionSalesDetailObjectTypeEnum(int value) {
        this.value = value;
    }
}
